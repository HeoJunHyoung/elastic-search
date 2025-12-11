package com.example.elasticsearch.domain.snack.controller;

import com.example.elasticsearch.domain.snack.dto.request.SnackRequest;
import com.example.elasticsearch.domain.snack.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SnackController {

    private final SnackService snackService;

    @PostMapping("/api/snacks")
    public String createSnack(@RequestBody SnackRequest request) {
        snackService.createSnack(request);
        return "과자 등록 완료 (검색 반영까지 약간의 시간이 소요될 수 있습니다)";
    }

}