package com.example.authservice.auth;

import com.example.authservice.interfaces.IAuthService;
import com.example.authservice.client.UserClient;
import com.example.authservice.confirmation.ConfirmationMessage;
import com.example.authservice.interfaces.IMessageService;
import com.example.authservice.interfaces.ITokenService;
import com.example.authservice.repository.ConfirmationRepository;
import com.example.authservice.repository.TokenRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.token.Token;
import com.example.authservice.token.TokenType;
import com.example.authservice.confirmation.Confirmation;
import com.example.authservice.user.Role;
import com.example.authservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService
{
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ConfirmationRepository confirmationRepository;
    private final ITokenService tokenService;
    private final IMessageService messageService;

    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request)
    {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) //TODO: Role management
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build();

        userRepository.save(user);
        var confirmation = new Confirmation(user);

        messageService.sendMessage(ConfirmationMessage.builder()
                .email(confirmation.getUser().getEmail())
                .token(confirmation.getToken()).build());

        confirmationRepository.save(confirmation);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var savedUser = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = tokenService.generateToken(savedUser);

        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .userToken(Base64.getEncoder().encodeToString(savedUser.getId().getBytes()))
                .build();
    }

    @Override
    public void saveUserToken(User savedUser, String jwtToken)
    {
        Token token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .user(savedUser)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }


    @Override
    public void revokeAllUserTokens(User user)
    {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t ->
        {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void enableUser(String confirmation)
    {
        var currentConfirmation = confirmationRepository.findByToken(confirmation).get();
        var user = currentConfirmation.getUser();

        if (currentConfirmation.getValidUntil().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Confirmation is Expired");

        userClient.registerUser(user.getUsername());

        user.setEnabled(true);

        userRepository.save(user);
        confirmationRepository.deleteById(currentConfirmation.getId());
    }

    @Override
    public boolean doesUserHasRole(String username, String role)
    {
        return role.equals(userRepository.findUserByUsername(username).get().getRole().toString());
    }

}
