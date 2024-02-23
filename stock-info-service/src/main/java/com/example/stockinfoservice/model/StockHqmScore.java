package com.example.stockinfoservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class StockHqmScore {
    @JsonProperty("HQMScore")
    private Double HQMScore;
    private Double oneMonthPriceReturn;
    private Double oneMonthReturnPercentile;
    private Double oneYearPriceReturn;
    private Double oneYearReturnPercentile;
    private Double sixMonthPriceReturn;
    private Double sixMonthReturnPercentile;
    private String ticker;
    private Double threeMonthPriceReturn;
    private Double threeMonthReturnPercentile;
}
