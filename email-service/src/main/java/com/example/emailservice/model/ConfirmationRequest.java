package com.example.emailservice.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class ConfirmationRequest
{
    @Email
    private String email;

    @Length(min = 10)
    private String token;
}
