package com.example.authservice.repository;

import com.example.authservice.confirmation.Confirmation;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long>
{
    Optional<Confirmation> findByToken(String confirmation);
}
