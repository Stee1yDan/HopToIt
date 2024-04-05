package com.example.userservice;

import com.example.userservice.model.Portfolio;
import com.example.userservice.model.Stock;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@RequiredArgsConstructor
public class UserServiceApplication {

	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner runner()
	{
		return args ->
		{
//			Map<String, Integer> stocks = new HashMap<>();
//			stocks.put("APPL",4);
//			stocks.put("BTC-USD",1);
			List<Stock> stocks = new ArrayList<>();
			stocks.add(new Stock(null, "AAPL", 4));
			stocks.add(new Stock(null, "MSFT", 10));

			Portfolio portfolio = new Portfolio(1L,"Portfolio_1", 0.3,0.3,0.3,0.3, 0.3, 1000D, stocks);
			userService.saveUser(
					User.builder()
							.id(1L)
							.username("admin")
							.description("Hello world")
							.portfolios(List.of(portfolio))
							.build()

			);
		};
	}

}
