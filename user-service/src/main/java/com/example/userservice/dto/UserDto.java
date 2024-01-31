package com.example.userservice.dto;

import com.example.userservice.model.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto
{
    private String username;
    private String description;
    private List<PortfolioDto> portfolios;
}
