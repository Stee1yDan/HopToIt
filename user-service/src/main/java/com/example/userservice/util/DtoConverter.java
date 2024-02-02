package com.example.userservice.util;

import com.example.userservice.dto.PortfolioDto;
import com.example.userservice.dto.StockDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.Portfolio;
import com.example.userservice.model.Stock;
import com.example.userservice.model.User;

import java.util.stream.Collectors;

public class DtoConverter
{
    public static StockDto convertStock(Stock stock)
    {
        return StockDto.builder()
                .name(stock.getName())
                .count(stock.getCount())
                .build();
    }
    public static PortfolioDto convertPortfolio(Portfolio portfolio)
    {
        return PortfolioDto.builder()
                .name(portfolio.getName())
                .stocks(portfolio.getStocks().stream()
                        .map(stock -> convertStock(stock)).
                        collect(Collectors.toList()))
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
