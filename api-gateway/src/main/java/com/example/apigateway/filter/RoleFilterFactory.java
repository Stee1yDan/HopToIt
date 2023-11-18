package com.example.apigateway.filter;

import com.example.apigateway.config.JwtService;
import com.example.apigateway.repository.UserRepository;
import com.example.apigateway.user.Role;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@AllArgsConstructor
public class RoleFilterFactory
{
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public RoleFilter getRoleFilter(List<Role> roles)
    {
        return new RoleFilter(this.jwtService,this.userRepository,roles);
    }
}
