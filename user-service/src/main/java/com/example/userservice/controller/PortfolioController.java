package com.example.userservice.controller;

import com.example.userservice.model.Portfolio;
import com.example.userservice.interfaces.IPortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/portfolios")
@RequiredArgsConstructor
@Slf4j
public class PortfolioController
{
    private final IPortfolioService portfolioService;

    @PutMapping("/update/{username}/{name}")
    public CompletableFuture<ResponseEntity<Void>> updatePortfolio(@RequestBody Portfolio portfolio,
                                                                   @PathVariable("username") String username,
                                                                   @PathVariable("name") String name)
    {
        return CompletableFuture.supplyAsync(() -> {portfolioService.updatePortfolio(portfolio,username,name);;
            return null;
        });
    }


    @PostMapping("/add/{username}")
    public CompletableFuture<ResponseEntity<Void>> addPortfolio(@RequestBody Portfolio portfolio, @PathVariable("username") String username)
    {
        return CompletableFuture.supplyAsync(() -> {portfolioService.addPortfolio(portfolio, username);
            return null;
        });

    }

    @DeleteMapping("/delete/{username}/{name}")
    public CompletableFuture<ResponseEntity<Void>> deletePortfolio(@PathVariable("username") String username, @PathVariable("name") String name)
    {
        return CompletableFuture.supplyAsync(() -> {portfolioService.deletePortfolio(username,name);;
            return null;
        });
    }


}
