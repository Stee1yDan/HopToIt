package com.example.stockinfoservice.repository;

import com.example.stockinfoservice.model.StockHqmScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockHqmScoreRepository extends JpaRepository<StockHqmScore, String> {
}
