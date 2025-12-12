package com.example.elasticsearch.domain.product.service;

import com.example.elasticsearch.domain.product.dto.request.ProductCreateRequest;
import com.example.elasticsearch.domain.product.dto.response.ProductResponse;
import com.example.elasticsearch.domain.product.entity.ProductEntity;
import com.example.elasticsearch.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProductApi(ProductCreateRequest request) {

        ProductEntity productEntity = ProductEntity.of(
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getQuantity(),
                request.getCategory()
        );
        productRepository.save(productEntity);
    }

    public List<ProductResponse> findAllApi() {
        return productRepository.findAll()
                .stream()
                .map(p -> ProductResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .price(p.getPrice())
                        .quantity(p.getQuantity())
                        .category(p.getCategory())
                        .build()
                )
                .collect(Collectors.toList());
    }


}
