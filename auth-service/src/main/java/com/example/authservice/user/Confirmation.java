package com.example.authservice.user;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "confirmations")
public class Confirmation
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime validUntil;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;

    public Confirmation(User user)
    {
        this.user = user;
        validUntil = LocalDateTime.now().plusDays(1);
        this.token = UUID.randomUUID().toString();
    }

}