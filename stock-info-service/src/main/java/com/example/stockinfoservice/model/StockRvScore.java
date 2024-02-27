package com.example.stockinfoservice.model;

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
@Transactional
public class StockRvScore {
    private Double evToEbitda;
    private Double evToEbitdaPercentile;
    private Double evToGp;
    private Double evToGpPercentile;
    private Double pbPercentile;
    private Double pbRatio;
    private Double pePercentile;
    private Double peRatio;
    private Double psPercentile;
    private Double psRatio;
    private Double rvScore;
    @Id
    private String ticker;
}
