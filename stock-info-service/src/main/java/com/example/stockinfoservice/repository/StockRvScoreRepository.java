package com.example.stockinfoservice.repository;

import com.example.stockinfoservice.model.StockRvScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRvScoreRepository extends JpaRepository<StockRvScore, String> {
}
