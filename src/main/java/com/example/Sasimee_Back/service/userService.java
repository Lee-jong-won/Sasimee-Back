package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class userService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;

}
