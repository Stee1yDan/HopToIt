package com.example.userservice.service;

import com.example.userservice.client.PortfolioClient;
import com.example.userservice.dto.PortfolioDto;
import com.example.userservice.model.Portfolio;
import com.example.userservice.model.User;
import com.example.userservice.repository.PortfolioRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.interfaces.IPortfolioService;
import com.example.userservice.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService implements IPortfolioService
{
    private final UserRepository userRepository;
    private final PortfolioClient portfolioClient;
    @Override
    public void updatePortfolio(Portfolio portfolio, String username, String name)
    {
        User currentUser = userRepository.findUserByUsername(username);
        List<Portfolio> portfolios = currentUser.getPortfolios();

        if (portfolios.stream()
                .map(Portfolio::getName)
                .toList().contains(portfolio.getName()))
        {
            PortfolioDto portfolioDto = portfolioClient.calculatePortfolioMetrics(DtoConverter.convertPortfolio(portfolio));
            Portfolio currentPortfolio = DtoConverter.convertPortfolioDto(portfolioDto);
            portfolios.removeIf(p -> p.getName().equals(name));
            portfolios.add(currentPortfolio);
        }



        userRepository.save(currentUser);
    }

    @Override
    public void addPortfolio(Portfolio portfolio, String username)
    {
        User currentUser = userRepository.findUserByUsername(username);
        List<Portfolio> portfolios = currentUser.getPortfolios();

        if (portfolios.stream() //TODO: Something
                .map(Portfolio::getName)
                .toList().contains(portfolio.getName()))
        {
            return;
        }

        PortfolioDto portfolioDto = portfolioClient.calculatePortfolioMetrics(DtoConverter.convertPortfolio(portfolio));
        Portfolio currentPortfolio = DtoConverter.convertPortfolioDto(portfolioDto);

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
