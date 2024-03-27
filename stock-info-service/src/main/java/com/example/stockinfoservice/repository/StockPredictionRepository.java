package com.example.stockinfoservice.repository;

import com.example.stockinfoservice.model.StockPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Repository
public interface StockPredictionRepository extends JpaRepository<StockPrediction,String> {
    Optional<List<StockPrediction>> findByTicker(String ticker);
}
