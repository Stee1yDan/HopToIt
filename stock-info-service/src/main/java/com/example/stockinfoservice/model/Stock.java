package com.example.stockinfoservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Stock
{
    private String symbol;
    private String longName;

    private String sector;
    private String website;
    private String longBusinessSummary;
    private Double beta;
    private Double ask;
    private Double dividendRate;
    private Double dividendYield;
    private Double currentPrice;
    private Double dayHigh;
    private Double dayLow;

}
