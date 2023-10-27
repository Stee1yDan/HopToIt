package com.example.authservice.auth;

import com.example.authservice.config.JwtService;
import com.example.authservice.repository.TokenRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.token.Token;
import com.example.authservice.token.TokenType;
import com.example.authservice.user.Role;
import com.example.authservice.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthResponse register(RegisterRequest request)
    {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) //TODO: Role management
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(Base64.getEncoder().encodeToString(savedUser.getId().getBytes()))
                .build();
    }


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
        var jwtToken = jwtService.generateToken(savedUser);

        revokeAllUserTokens(savedUser);
        saveUserToken(savedUser, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(Base64.getEncoder().encodeToString(savedUser.getId().getBytes()))
                .build();
    }

    private void saveUserToken(User savedUser, String jwtToken)
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

    private void revokeAllUserTokens(User user)
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

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            return;
        }

        final String username;

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null)
        {
            UserDetails userDetails = this.userRepository.findUserByUsername(username).orElseThrow();

            if (jwtService.isRefreshTokenValid(refreshToken,userDetails))
            {
                var accessToken = jwtService.generateToken(userDetails);
                revokeAllUserTokens((User) userDetails);
                saveUserToken((User) userDetails, accessToken);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }


        }
    }
}
