package com.example.elasticsearch.domain.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.example.elasticsearch.domain.product.document.ProductDocument;
import com.example.elasticsearch.domain.product.entity.ProductEntity;
import com.example.elasticsearch.domain.product.event.ProductSyncEvent;
import com.example.elasticsearch.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductSyncService {

    private final ElasticsearchClient esClient;
    private final ProductRepository productRepository;

    /**
     * @Retryable 설정 설명:
     * - value = Exception.class: Exception 발생 시 재시도 수행
     * - maxAttempts = 3: 최대 3회 시도 (최초 1회 + 재시도 2회)
     * - backoff: 1000ms(1초) 대기, multiplier=2 (실패할 때마다 대기시간 2배 증가)
     */
    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void syncToElasticsearch(ProductSyncEvent event) {
        try {
            if (event.getOperation() == ProductSyncEvent.SyncOperation.DELETE) {
                syncDelete(event.getProductId());
            } else {
                syncCreateOrUpdate(event.getProductId());
            }
        } catch (Exception e) {
            log.error("ES 동기화 시도 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException(e); // 예외를 던져야 @Retryable이 감지하고 재시도함
        }
    }

    /**
     * @Recover: 재시도 3회를 모두 실패했을 때 실행되는 메서드 (Fallback)
     * - 메서드 파라미터로 발생한 예외와, 원래 메서드의 인자를 받을 수 있음.
     * - 반환 타입은 원래 메서드(syncToElasticsearch)와 맞춰야 함 (여기선 void).
     */
    @Recover
    public void recover(Exception e, ProductSyncEvent event) {
        log.error("CRITICAL: 3회 재시도 후에도 ES 동기화 최종 실패. ProductID: {}", event.getProductId());
        log.error("원인: ", e);

        // 여기서 'Dead Letter Queue(DLQ)' 테이블에 저장하거나 슬랙/이메일 알림을 보내 개발자가 수동 처리하도록 조치함.
    }

    private void syncCreateOrUpdate(String productId) throws IOException {
        ProductEntity entity = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new RuntimeException("Product not found via Sync: " + productId));

        ProductDocument document = ProductDocument.from(entity);

        IndexRequest<ProductDocument> request = IndexRequest.of(b -> b
                .index("products")
                .id(productId)
                .document(document)
        );

        esClient.index(request);
        log.info("ES 동기화 성공: {}", productId);
    }

    private void syncDelete(String productId) throws IOException {
        DeleteRequest request = DeleteRequest.of(b -> b
                .index("products")
                .id(productId)
        );
        esClient.delete(request);
        log.info("ES 삭제 성공: {}", productId);
    }
}