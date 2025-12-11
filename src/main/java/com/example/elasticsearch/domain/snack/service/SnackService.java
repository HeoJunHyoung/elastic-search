package com.example.elasticsearch.domain.snack.service;

import com.example.elasticsearch.domain.snack.document.SnackDocument;
import com.example.elasticsearch.domain.snack.dto.request.SnackRequest;
import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import com.example.elasticsearch.domain.snack.event.SnackCreatedEvent;
import com.example.elasticsearch.domain.snack.repository.SnackRepository;
import com.example.elasticsearch.domain.snack.repository.SnackSearchRepository; // 추가
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnackService {

    private final SnackRepository snackRepository;
    private final SnackSearchRepository snackSearchRepository; // 주입 추가
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createSnack(SnackRequest request) {
        SnackEntity savedSnack = snackRepository.save(request.toEntity());
        eventPublisher.publishEvent(new SnackCreatedEvent(savedSnack));
    }

    // 과자 검색 (Elasticsearch)
    @Transactional(readOnly = true)
    public List<SnackDocument> searchSnacks(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            // 키워드가 없으면 전체 조회 (실무에서는 페이징 처리 권장)
            List<SnackDocument> list = new ArrayList<>();
            snackSearchRepository.findAll().forEach(list::add);
            return list;
        }
        return snackSearchRepository.findByName(keyword);
    }

    // 과자 상세 조회 (Elasticsearch ID 기준)
    @Transactional(readOnly = true)
    public SnackDocument getSnackDetail(String id) {
        return snackSearchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 과자를 찾을 수 없습니다. ID: " + id));
    }
}