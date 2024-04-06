package com.example.userservice.client;

import com.example.userservice.dto.PortfolioDto;
import com.example.userservice.model.EfficientFrontier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("portfolio-performance-service")
public interface PortfolioClient {
    @PostMapping(value = "/api/v1/portfolioAnalysis/calculateMetrics")
    PortfolioDto calculatePortfolioMetrics(@RequestBody PortfolioDto request);

    @PostMapping(value = "/api/v1/portfolioAnalysis/getEfficientFrontier")
    EfficientFrontier getEfficientFrontier(@RequestBody PortfolioDto request);
}
