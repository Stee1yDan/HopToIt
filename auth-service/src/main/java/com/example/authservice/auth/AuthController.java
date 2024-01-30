package com.example.authservice.auth;

import com.example.authservice.config.JwtService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController
{
    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/enable/{confirmation}")
    @CircuitBreaker(name="auth-controller", fallbackMethod = "fallbackEnableMethod")
    @Retry(name="auth-controller")
    public CompletableFuture<ResponseEntity<Void>> enable(@PathVariable("confirmation") String confirmation)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            authService.enableUser(confirmation);
            return new ResponseEntity<>(HttpStatus.OK);
        });
    }

    @GetMapping("/check/{username}/{role}")
    @CircuitBreaker(name="auth-controller", fallbackMethod = "fallbackRoleMethod")
    @TimeLimiter(name="auth-controller")
    @Retry(name="auth-controller")
    public CompletableFuture<ResponseEntity<Boolean>> doesUserHasRole(@PathVariable("username") String username,
                                                   @PathVariable("role") String role)
    {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(authService.doesUserHasRole(username,role)));
    }

    @GetMapping("/check/{token}")
    @CircuitBreaker(name="auth-controller", fallbackMethod = "fallbackTokenMethod")
    @TimeLimiter(name="auth-controller")
    @Retry(name="auth-controller")
    public CompletableFuture<ResponseEntity<Boolean>> isTokenValid(@PathVariable("token") String token)
    {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(jwtService.isTokenValid(token)));
    }

    @PostMapping("/register")
    @CircuitBreaker(name="auth-controller", fallbackMethod = "fallbackRegisterMethod")
    @Retry(name="auth-controller")
    public CompletableFuture<ResponseEntity<RegisterResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        System.out.println("ASDDDDDDDDDDDDS");
        return CompletableFuture.supplyAsync(() ->
        {
            System.out.println(authService.isUsernameNotUnique(request.getUsername()));
            System.out.println((authService.isEmailNotUnique(request.getEmail())));
            if (authService.isUsernameNotUnique(request.getUsername()))
                return new ResponseEntity<>(new RegisterResponse("Username is already taken."),
                        HttpStatus.BAD_REQUEST);

            if (authService.isEmailNotUnique(request.getEmail()))
                return new ResponseEntity<>(new RegisterResponse("Email is already in use."),
                        HttpStatus.BAD_REQUEST);

            authService.register(request);

            return new ResponseEntity<>(new RegisterResponse("Confirmation letter was sent to your email."),
                    HttpStatus.CREATED);
        });
    }
    @PostMapping("/authenticate")
    @CircuitBreaker(name="auth-controller", fallbackMethod = "fallbackAuthMethod")
    @Retry(name="auth-controller")
    public CompletableFuture<ResponseEntity<AuthResponse>> authenticate(
            @RequestBody AuthRequest request
    ) {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(authService.authenticate(request), HttpStatus.OK));
    }

    public CompletableFuture<ResponseEntity<Void>> fallbackEnableMethod(String token, Throwable throwable)
    {
        log.info("WARNING! Couldn't enable the user", throwable); // TODO: Send an email to admin AND delete user
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<Boolean>> fallbackRoleMethod(String token, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(false, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<Boolean>> fallbackTokenMethod(String username, String role, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(false, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<RegisterResponse>> fallbackRegisterMethod(RegisterRequest request, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(new RegisterResponse("Something went wrong. Try again."),
                HttpStatus.BAD_REQUEST));
    }

    public CompletableFuture<ResponseEntity<Void>> fallbackAuthMethod(AuthRequest request, Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
