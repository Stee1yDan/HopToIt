package com.example.userservice;

import com.example.userservice.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class UserServiceApplication {

	private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner()
//	{
//		return args ->
//		{
//			Map<String, Integer> stocks = new HashMap<>();
//			stocks.put("APPL",4);
//			stocks.put("BTC-USD",1);
//
//			Portfolio portfolio = new Portfolio(1L,"Portfolio_1", stocks);
//			userService.saveUser(
//					User.builder()
//							.id(1L)
//							.username("Bobson1")
//							.description("Hello world")
//							.firstname("Dan")
//							.lastname("Vs")
//							.portfolios(List.of(portfolio))
//							.build()
//
//			);
//		};
//	}

}
