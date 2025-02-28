package com.moldavets.microservices.api_gateway.service.Impl;

import com.moldavets.microservices.api_gateway.entity.Role;
import com.moldavets.microservices.api_gateway.entity.User;
import com.moldavets.microservices.api_gateway.repository.RoleRepository;
import com.moldavets.microservices.api_gateway.repository.UserRepository;
import com.moldavets.microservices.api_gateway.service.RoleService;
import com.moldavets.microservices.api_gateway.service.UserService;
import com.moldavets.microservices.api_gateway.util.StatusEnum;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(StatusEnum.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN UserServiceImpl.register() - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN UserServiceImpl.getAll() - users: {}", result);
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN UserServiceImpl.findByUsername() - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if(result == null) {
            log.warn("IN UserServiceImpl.findById() - user: {} not found", id);
            return null;
        }

        log.info("IN UserServiceImpl.findById() - user: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("IN UserServiceImpl.deleteById() - user: {} deleted", id);
    }
}
