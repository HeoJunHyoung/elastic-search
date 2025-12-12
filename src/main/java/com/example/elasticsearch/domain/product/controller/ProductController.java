package com.example.elasticsearch.domain.product.controller;

import com.example.elasticsearch.domain.product.dto.request.ProductCreateRequest;
import com.example.elasticsearch.domain.product.dto.response.ProductResponse;
import com.example.elasticsearch.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/posts")
    public void createProduct(@RequestBody ProductCreateRequest request) {
        productService.createProductApi(request);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> responses = productService.findAllApi();
        return ResponseEntity.ok(responses);
    }

    // 요청 예시: GET /posts/search?keyword=아이폰
    @GetMapping("/posts/search")
    public ResponseEntity<List<ProductResponse>> search(@RequestParam String keyword) {
        List<ProductResponse> responses = productService.search(keyword);
        return ResponseEntity.ok(responses);
    }

}
