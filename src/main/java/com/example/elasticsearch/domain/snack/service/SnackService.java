package com.example.elasticsearch.domain.snack.service;

import com.example.elasticsearch.domain.snack.dto.request.SnackRequest;
import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import com.example.elasticsearch.domain.snack.event.SnackCreatedEvent;
import com.example.elasticsearch.domain.snack.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SnackService {

    private final SnackRepository snackRepository;
    private final ApplicationEventPublisher eventPublisher; // 이벤트 발행기

    @Transactional
    public void createSnack(SnackRequest request) {
        // 1. MySQL 저장
        SnackEntity savedSnack = snackRepository.save(request.toEntity());

        // 2. 이벤트 발행 (Elasticsearch 인덱싱 요청)
        // 트랜잭션이 커밋된 후, 리스너가 비동기로 동작함
        eventPublisher.publishEvent(new SnackCreatedEvent(savedSnack));
    }
}