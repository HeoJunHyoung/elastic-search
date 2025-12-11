package com.example.elasticsearch.domain.snack.repository;

import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackRepository extends JpaRepository<SnackEntity, Long> {
    // 필요 시 추가 메서드 정의 (예: 브랜드로 찾기 등)
    // List<SnackEntity> findByBrand(String brand);
}