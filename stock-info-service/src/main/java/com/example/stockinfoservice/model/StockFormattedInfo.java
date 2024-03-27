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
public class StockFormattedInfo
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
    private Double marketCap;
    private Double hqmScore;
    private Double rvScore;
    private Double prediction;
    private Double hourlyChange;
    private Double dailyChange;
    private Double weeklyChange;
    private Double monthlyChange;
}
