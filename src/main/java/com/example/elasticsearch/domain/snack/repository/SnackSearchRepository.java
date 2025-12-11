package com.example.elasticsearch.domain.snack.repository;

import com.example.elasticsearch.domain.snack.document.SnackDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SnackSearchRepository extends ElasticsearchRepository<SnackDocument, String> {

    // 상품명(name)에 키워드가 포함된 문서 검색
    List<SnackDocument> findByName(String keyword);

    // 브랜드 일치 검색 예시
    List<SnackDocument> findByBrand(String brand);
}