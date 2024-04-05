package com.example.userservice.dto;

import com.example.userservice.model.Stock;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDto
{
    private String name;

    private Double expectedReturn;

    private Double sharpe;

    private Double volatility;

    private Double portfolioPrice;

    private Double freeFunds;

    private Double variance;

    private List<StockDto> stocks;
}
