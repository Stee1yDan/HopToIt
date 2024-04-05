package com.example.userservice.utils;

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

    public static Stock convertStockDto(StockDto stockDto)
    {
        return Stock.builder()
                .name(stockDto.getName())
                .count(stockDto.getCount())
                .build();
    }
    public static PortfolioDto convertPortfolio(Portfolio portfolio)
    {
        return PortfolioDto.builder()
                .name(portfolio.getName())
                .volatility(portfolio.getVolatility())
                .sharpe(portfolio.getSharpe())
                .variance(portfolio.getVariance())
                .expectedReturn(portfolio.getExpectedReturn())
                .portfolioPrice(portfolio.getPortfolioPrice())
                .freeFunds(portfolio.getFreeFunds())
                .stocks(portfolio.getStocks().stream()
                        .map(DtoConverter::convertStock).
                        collect(Collectors.toList()))
                .build();
    }

    public static Portfolio convertPortfolioDto(PortfolioDto portfolioDto)
    {
        return Portfolio.builder()
                .name(portfolioDto.getName())
                .volatility(portfolioDto.getVolatility())
                .sharpe(portfolioDto.getSharpe())
                .variance(portfolioDto.getVariance())
                .expectedReturn(portfolioDto.getExpectedReturn())
                .portfolioPrice(portfolioDto.getPortfolioPrice())
                .freeFunds(portfolioDto.getFreeFunds())
                .stocks(portfolioDto.getStocks().stream()
                        .map(DtoConverter::convertStockDto).
                        collect(Collectors.toList()))
                .build();
    }

    public static UserDto convertUser(User user)
    {
        return UserDto.builder()
                .username(user.getUsername())
                .description(user.getDescription())
                .portfolios(user.getPortfolios().stream()
                        .map(DtoConverter::convertPortfolio)
                        .collect(Collectors.toList()))
                .build();
    }
}
