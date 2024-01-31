package com.example.authservice.repository;

import com.example.authservice.user.User;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String>
{
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);
}
