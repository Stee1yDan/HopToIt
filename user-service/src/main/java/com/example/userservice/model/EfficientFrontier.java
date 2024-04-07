package com.example.userservice.model;

import com.example.userservice.dto.PortfolioDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EfficientFrontier {
    private PortfolioDto maxSharpePortfolio;
    private PortfolioDto maxReturnsPortfolio;
    private PortfolioDto minVolatilityPortfolio;
    private List<PortfolioDto> efficientFrontier;
}
