package com.patmgt.authservice.service;

import com.patmgt.authservice.model.User;
import com.patmgt.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    public final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
