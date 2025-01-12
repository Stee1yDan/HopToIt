package com.example.userservice.repository;

import com.example.userservice.model.User;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

    User findUserByUsername(String username);
    User deleteUserByUsername(String username);
}
