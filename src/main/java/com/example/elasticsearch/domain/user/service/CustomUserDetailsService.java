package com.example.elasticsearch.domain.user.service;

import com.example.elasticsearch.domain.user.dto.request.JoinRequest;
import com.example.elasticsearch.domain.user.entity.UserEntity;
import com.example.elasticsearch.domain.user.entity.enumerate.Role;
import com.example.elasticsearch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입 로직
    public void joinProcess(JoinRequest joinRequest) {

        boolean isUser = userRepository.existsByUsername(joinRequest.getUsername());

        if (isUser) {
            return;
        }

        UserEntity userEntity = UserEntity.builder()
                .username(joinRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(joinRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(userEntity);
    }

    // Spring Security가 로그인 시 실행하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        if (userData != null) {
            return User.builder()
                    .username(userData.getUsername())
                    .password(userData.getPassword())
                    .roles(String.valueOf(Role.USER))
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
