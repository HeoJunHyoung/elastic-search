package com.example.elasticsearch.domain.product.repository;

import com.example.elasticsearch.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
