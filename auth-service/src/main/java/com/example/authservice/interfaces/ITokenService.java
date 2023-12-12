package com.example.authservice.interfaces;

import com.example.authservice.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

public interface ITokenService
{
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails);

    boolean isTokenValid(String token);
    boolean isTokenValidByUser(String token, UserDetails userDetails);
}
