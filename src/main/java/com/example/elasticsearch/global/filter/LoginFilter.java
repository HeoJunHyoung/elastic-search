package com.example.elasticsearch.global.filter;

import com.example.elasticsearch.domain.user.dto.request.JoinRequest;
import com.example.elasticsearch.global.util.CustomUserDetails;
import com.example.elasticsearch.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // 1. 로그인 시도: 요청 데이터를 받아 인증 객체 생성
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // JSON 요청 본문에서 username, password 추출 (ObjectMapper 사용)
            ObjectMapper om = new ObjectMapper();
            JoinRequest loginData = om.readValue(request.getInputStream(), JoinRequest.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginData.getUsername(), loginData.getPassword(), null
            );

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 2. 로그인 성공 시: JWT 생성 후 쿠키에 담아 응답
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

        // 1. Principal을 CustomUserDetails로 캐스팅
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();

        // 2. Role 추출
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 3. 유저 ID 추출
        Long userId = customUserDetails.getId();

        // 4. 토큰 생성 시 userId 포함
        String token = jwtUtil.createJwt(userId, username, role, 60*60*1000L);

        // 쿠키 생성
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true); // 자바스크립트 접근 불가 (XSS 방지)
        cookie.setPath("/");      // 모든 경로에서 쿠키 유효
        cookie.setMaxAge(60 * 60); // 쿠키 만료 시간 (초 단위)
        // cookie.setSecure(true); // HTTPS를 사용하는 경우 주석 해제

        // 응답에 쿠키 추가
        response.addCookie(cookie);
    }

    // 3. 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}