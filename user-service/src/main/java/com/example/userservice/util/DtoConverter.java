package com.example.userservice.util;

import com.example.userservice.dto.PortfolioDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.Portfolio;
import com.example.userservice.model.User;

import java.util.stream.Collectors;

public class DtoConverter
{
    public static PortfolioDto convertPortfolio(Portfolio portfolio)
    {
        return PortfolioDto.builder()
                .name(portfolio.getName())
                .stock(portfolio.getStock())
                .build();
    }

    public static UserDto convertUser(User user)
    {
        return UserDto.builder()
                .username(user.getUsername())
                .description(user.getDescription())
                .portfolios(user.getPortfolios().stream()
                        .map(portfolio -> convertPortfolio(portfolio))
                        .collect(Collectors.toList()))
                .build();
    }
}
