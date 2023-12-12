package com.example.authservice.auth;

import com.example.authservice.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/enable/{confirmation}")
    @ResponseStatus(HttpStatus.OK)
    public void enable(@PathVariable("confirmation") String confirmation)
    {
        authService.enableUser(confirmation);
    }

    @GetMapping("/check/{username}/{role}")
    public ResponseEntity<Boolean> doesUserHasRole(@PathVariable("username") String username,
                                                   @PathVariable("role") String role)
    {
        return ResponseEntity.ok(authService.doesUserHasRole(username,role));
    }

    @GetMapping("/check/{token}")
    public ResponseEntity<Boolean> isTokenValid(@PathVariable("token") String token)
    {
        return ResponseEntity.ok(jwtService.isTokenValid(token));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody RegisterRequest request
    ) {
        authService.register(request);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
