package com.example.elasticsearch.domain.snack.repository;

import com.example.elasticsearch.domain.snack.document.SnackDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SnackSearchRepository extends ElasticsearchRepository<SnackDocument, String> {
    // 기본 CRUD 외에 필요한 검색 메서드를 정의할 수 있음
    // 예: 이름에 특정 키워드가 포함된 과자 검색
    // List<SnackDocument> findByNameContains(String keyword);
}