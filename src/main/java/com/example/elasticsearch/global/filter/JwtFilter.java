package com.example.elasticsearch.global.filter;

import com.example.elasticsearch.domain.user.entity.UserEntity;
import com.example.elasticsearch.domain.user.entity.enumerate.Role;
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

        // 토큰에서 username, role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // UserDetails 객체 생성 (비밀번호는 필요 없음, 이미 검증됨)
        // 주의: DB 조회를 하지 않고 토큰 정보로만 인증 객체를 만듦 (성능 최적화)
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("temppassword") // 임의값
                .role(Role.valueOf(role))
                .build();

        UserDetails userDetails = User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .build();

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 세션(SecurityContext)에 사용자 등록 -> 이번 요청 동안만 유효
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}