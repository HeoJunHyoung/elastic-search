package com.example.elasticsearch.domain.user.controller;

import com.example.elasticsearch.domain.user.dto.request.JoinRequest;
import com.example.elasticsearch.domain.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
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

}
