package com.example.elasticsearch.domain.snack.event;

import com.example.elasticsearch.domain.snack.document.SnackDocument;
import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import com.example.elasticsearch.domain.snack.repository.SnackSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnackEventListener {

    private final SnackSearchRepository snackSearchRepository;

    @Async // 이 메서드는 별도의 스레드에서 비동기로 실행됨
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // DB 트랜잭션이 성공(Commit)한 후에만 실행
    public void handleSnackCreatedEvent(SnackCreatedEvent event) {
        SnackEntity entity = event.getSnackEntity();

        try {
            // Entity -> Document 변환
            SnackDocument snackDocument = SnackDocument.from(entity);

            // Elasticsearch 저장
            snackSearchRepository.save(snackDocument);

            log.info("Elasticsearch Indexing Success: ID={}, Name={}", entity.getId(), entity.getName());

        } catch (Exception e) {
            log.error("Elasticsearch Indexing Failed: ID={}", entity.getId(), e);
            // 실무에서는 여기서 실패 로그를 별도 테이블에 저장하거나 재시도 로직(Retry)을 구현함
        }
    }
}