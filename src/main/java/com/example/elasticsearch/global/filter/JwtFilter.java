package com.example.elasticsearch.global.filter;

import com.example.elasticsearch.domain.user.entity.UserEntity;
import com.example.elasticsearch.domain.user.entity.enumerate.Role;
import com.example.elasticsearch.global.util.CustomUserDetails;
import com.example.elasticsearch.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        Cookie[] cookies = request.getCookies();

        // 쿠키에서 accessToken 찾기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 토큰이 없거나 만료되었으면 다음 필터로 진행 (인증 안 된 상태)
        if (token == null || jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token); // 토큰에서 꺼낸 값: "ROLE_USER"
        Long userId = jwtUtil.getUserId(token);

        // [핵심 수정] "ROLE_" 접두사 제거 로직 추가
        String roleName = role;
        if (role.startsWith("ROLE_")) {
            roleName = role.substring(5); // "ROLE_" (5글자) 제거 -> "USER"
        }

        // UserEntity 생성
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username(username)
                .password("temppassword")
                .role(Role.valueOf(roleName)) // 이제 "USER"로 변환하므로 에러 없음
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}