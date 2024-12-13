package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("유저 찾을 수 없음1");
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));


        // 사용자 역할에 따라 권한 설정
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if ("ADMIN".equals(user.getRole().name())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // SasimeePrincipal에 권한 추가
        return new SasimeePrincipal(user.getEmail(), user.getEncryptPassword(), authorities);
    }
}
