package com.moldavets.microservices.api_gateway.repository;

import com.moldavets.microservices.api_gateway.entity.Role;
import com.moldavets.microservices.api_gateway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String username);
}
