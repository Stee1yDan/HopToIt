package com.example.authservice.repository;

import com.example.authservice.token.Token;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>
{
    @Query("SELECT t FROM tokens t JOIN users u ON (t.user.id = u.id) " +
            "WHERE u.id = :userId AND (t.expired = false OR t.revoked = false) ")
    List<Token> findAllValidTokensByUser(String userId);

    Optional<Token> findByToken(String token);
}
