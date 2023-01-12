package com.example.testtask.repos;

import com.example.testtask.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<User, Long> {
    boolean existsByName(String name);

    Optional<User> findByName(String usernameFromToken);
}
