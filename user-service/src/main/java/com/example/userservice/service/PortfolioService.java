package com.example.userservice.service;

import com.example.userservice.model.Portfolio;
import com.example.userservice.model.User;
import com.example.userservice.repository.PortfolioRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.interfaces.IPortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService implements IPortfolioService
{
    private final UserRepository userRepository;
    @Override
    public void updatePortfolio(Portfolio portfolio, String username, String name)
    {
        User currentUser = userRepository.findUserByUsername(username);
        List<Portfolio> portfolios = currentUser.getPortfolios();

        if (portfolios.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList()).contains(portfolio.getName()))
        {
            portfolios.removeIf(p -> p.getName().equals(name));
            portfolios.add(portfolio);
        }

        userRepository.save(currentUser);
    }

    @Override
    public void addPortfolio(Portfolio portfolio, String username)
    {
        User currentUser = userRepository.findUserByUsername(username);
        List<Portfolio> portfolios = currentUser.getPortfolios();

        Portfolio currentPortfolio = new Portfolio(null, portfolio.getName(), portfolio.getStocks());

        if (portfolios.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList()).contains(portfolio.getName()))
        {
            return;
        }

        portfolios.add(currentPortfolio);
        currentUser.setPortfolios(portfolios);
        userRepository.save(currentUser);
    }

    @Override
    public void deletePortfolio(String username, String name)
    {
        User currentUser = userRepository.findUserByUsername(username);
        List<Portfolio> portfolios = currentUser.getPortfolios();
        portfolios.removeIf(portfolio -> portfolio.getName().equals(name));
        currentUser.setPortfolios(portfolios);
        userRepository.save(currentUser);
    }

    @Override
    public Portfolio getPortfolio(String username, String name)
    {
        User currentUser = userRepository.findUserByUsername(username);
        List<Portfolio> portfolios = currentUser.getPortfolios();
        return portfolios.stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList()).get(0);

    }
}
