package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.UserDTO;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.exception.UserAlreadyExistsException;
import com.example.Sasimee_Back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;

    //토큰 만료시간은 1시간으로 설정
    private Long expiredMs = 1000 * 60 * 60l;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO.registerResponse register(UserDTO.registerRequest registerRequest)
    {
        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());

        if(userOptional.isPresent())
            throw new UserAlreadyExistsException("이미 해당 이메일로 가입된 계정이 존재합니다!");

        registerRequest.setPassword1(passwordEncoder.encode(registerRequest.getPassword1()));
        User user = UserDTO.registerRequest.toEntity(registerRequest);
        user.addUserAuthority();
        userRepository.save(user);
        return User.toRegisterResponseDTO(user);
    }


}
