package com.example.authservice.interfaces;

import com.example.authservice.user.User;

public interface IAuthService extends IUserAuthService
{

    void saveUserToken(User savedUser, String jwtToken);

    void revokeAllUserTokens(User user);
}
