package com.example.elasticsearch.domain.user.entity;

import com.example.elasticsearch.domain.user.entity.enumerate.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // 로그인 ID

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Enumerated(value = EnumType.STRING)
    private Role role; // 권한 (예: ROLE_USER, ROLE_ADMIN)

}