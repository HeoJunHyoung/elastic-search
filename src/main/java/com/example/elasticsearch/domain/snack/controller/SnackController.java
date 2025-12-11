package com.example.elasticsearch.domain.snack.controller;

import com.example.elasticsearch.domain.snack.document.SnackDocument;
import com.example.elasticsearch.domain.snack.dto.request.SnackRequest;
import com.example.elasticsearch.domain.snack.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SnackController {

    private final SnackService snackService;

    // 과자 등록
    @PostMapping("/api/snacks")
    public String createSnack(@RequestBody SnackRequest request) {
        snackService.createSnack(request);
        return "과자 등록 완료 (검색 반영까지 약간의 시간이 소요될 수 있습니다)";
    }

    // 과자 목록 검색 (키워드 파라미터 선택)
    // 예: GET /api/snacks?keyword=새우
    @GetMapping("/api/snacks")
    public ResponseEntity<List<SnackDocument>> searchSnacks(@RequestParam(required = false) String keyword) {
        List<SnackDocument> snacks = snackService.searchSnacks(keyword);
        return ResponseEntity.ok(snacks);
    }

    // 과자 상세 조회 (ID 기준)
    // 예: GET /api/snacks/1
    @GetMapping("/api/snacks/{id}")
    public ResponseEntity<SnackDocument> getSnackDetail(@PathVariable String id) {
        SnackDocument snack = snackService.getSnackDetail(id);
        return ResponseEntity.ok(snack);
    }
}