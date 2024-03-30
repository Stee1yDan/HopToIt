package com.example.stockinfoservice.repository;

import com.example.stockinfoservice.model.StockFormattedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockFormattedInfo, String> {
}
