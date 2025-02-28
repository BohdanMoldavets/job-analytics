package com.moldavets.microservices.api_gateway.service;

import com.moldavets.microservices.api_gateway.entity.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void deleteById(Long id);

}
