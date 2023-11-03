package com.example.authservice.repository;

import com.example.authservice.user.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long>
{
    Optional<Confirmation>  findByToken(String confirmation);
}
