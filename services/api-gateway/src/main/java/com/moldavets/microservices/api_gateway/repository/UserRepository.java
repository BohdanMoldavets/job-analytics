package com.moldavets.microservices.api_gateway.repository;

import com.moldavets.microservices.api_gateway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    void deleteById(Long id);
}
