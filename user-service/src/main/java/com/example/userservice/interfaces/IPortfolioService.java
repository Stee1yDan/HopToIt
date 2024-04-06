package com.example.userservice.interfaces;

import com.example.userservice.dto.PortfolioDto;
import com.example.userservice.model.EfficientFrontier;
import com.example.userservice.model.Portfolio;

import java.util.Optional;

public interface IPortfolioService
{
    void updatePortfolio(Portfolio portfolio, String username, String name);
    void addPortfolio(Portfolio portfolio, String username);

    void deletePortfolio(String username, String name);
    Portfolio getPortfolio(String username, String name);

    EfficientFrontier getEfficientFrontier(Portfolio portfolio);

}
