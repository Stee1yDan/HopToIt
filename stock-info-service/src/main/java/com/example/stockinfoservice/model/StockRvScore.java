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
public class StockRvScore {
    public Double evToEbitda;
    public Double evToEbitdaPercentile;
    public Double evToGp;
    public Double evToGpPercentile;
    public Double pbPercentile;
    public Double pbRatio;
    public Double pePercentile;
    public Double peRatio;
    public Double price;
    public Double psPercentile;
    public Double psRatio;
    public Double rvScore;
    public String ticker;
}
