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

}
