package com.example.apigateway.filter;

import com.example.apigateway.config.JwtService;
import com.example.apigateway.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Setter
@RequiredArgsConstructor
public class FilterFactory
{
    private final JwtService jwtService;
    private WebClient.Builder webClient;

    public RoleFilter getRoleFilter(Role role)
    {
        return new RoleFilter(this.jwtService,role);
    }

    public AuthFilter getAuthFilter()
    {
        return new AuthFilter();
    }

    public AuthUserFilter getAuthUserFilter()
    {
        return new AuthUserFilter();
    }
}
