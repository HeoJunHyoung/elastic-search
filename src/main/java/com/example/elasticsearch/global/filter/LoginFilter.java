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
import org.springframework.http.ResponseCookie;
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

        // 1. Principal에서 정보 추출 (기존 코드 유지)
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        Long userId = customUserDetails.getId();

        // 2. JWT 토큰 생성 (기존 코드 유지)
        String token = jwtUtil.createJwt(userId, username, role, 60*60*1000L);

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .path("/")
                .sameSite("None")  // 중요: 크로스 사이트 요청 허용
                .httpOnly(true)
                .secure(true)      // 중요: SameSite=None을 쓰려면 필수 (Localhost는 예외적으로 허용됨)
                .maxAge(60 * 60)
                .build();

        // 4. 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 3. 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}