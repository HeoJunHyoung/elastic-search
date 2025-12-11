package com.example.elasticsearch.domain.user.controller;

import com.example.elasticsearch.domain.user.dto.request.JoinRequest;
import com.example.elasticsearch.domain.user.service.CustomUserDetailsService;
import com.example.elasticsearch.global.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomUserDetailsService userService;

    @PostMapping("/joinProc")
    public void joinProcess(@RequestBody JoinRequest joinRequest) {
        userService.joinProcess(joinRequest);
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal CustomUserDetails authUser) {
        return "현재 요청 보낸 사용자 이름/아이디: "
                + authUser.getUsername() + ", " + authUser.getId();
    }

}
