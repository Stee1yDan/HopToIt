package com.example.stockinfoservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
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
@Entity
public class StockHqmScore {
    @Id
    private String ticker;
    @JsonProperty("HQMScore")
    private Double HQMScore;
    private Double oneMonthPriceReturn;
    private Double oneMonthReturnPercentile;
    private Double oneYearPriceReturn;
    private Double oneYearReturnPercentile;
    private Double sixMonthPriceReturn;
    private Double sixMonthReturnPercentile;
    private Double threeMonthPriceReturn;
    private Double threeMonthReturnPercentile;
}
