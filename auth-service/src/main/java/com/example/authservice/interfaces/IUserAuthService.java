package com.example.authservice.interfaces;

import com.example.authservice.auth.AuthRequest;
import com.example.authservice.auth.AuthResponse;
import com.example.authservice.auth.RegisterRequest;

public interface IUserAuthService
{
    void register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);

    boolean doesUserHasRole(String username, String role);

    void enableUser(String confirmation);

}
