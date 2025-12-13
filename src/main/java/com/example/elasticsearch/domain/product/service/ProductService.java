package com.example.elasticsearch.domain.product.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.elasticsearch.domain.product.document.ProductDocument;
import com.example.elasticsearch.domain.product.dto.request.ProductCreateRequest;
import com.example.elasticsearch.domain.product.dto.response.ProductResponse;
import com.example.elasticsearch.domain.product.entity.ProductEntity;
import com.example.elasticsearch.domain.product.event.ProductSyncEvent;
import com.example.elasticsearch.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductSyncService productSyncService;
    private final ElasticsearchClient elasticsearchClient;

    @Transactional
    public void createProductApi(ProductCreateRequest request) {

        ProductEntity productEntity = ProductEntity.of(
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getQuantity(),
                request.getCategory()
        );
        productRepository.save(productEntity);

        // 2. 이벤트 발행 (DB 커밋이 끝나면 리스너가 동작함)
        eventPublisher.publishEvent(ProductSyncEvent.builder()
                .productId(productEntity.getId().toString())
                .operation(ProductSyncEvent.SyncOperation.CREATE)
                .build());
    }

    // 트랜잭션 커밋 후 실행되는 리스너
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSyncEvent(ProductSyncEvent event) {
        log.info("동기화 이벤트 수신: {}", event.getProductId());
        productSyncService.syncToElasticsearch(event);
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

    public List<ProductResponse> search(String keyword) {
        try {
            SearchResponse<ProductDocument> response = elasticsearchClient.search(s -> s
                            .index("products") // 인덱스 명시
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .fields("title", "content") // 검색할 필드들
                                            .query(keyword)             // 검색어
                                            .fuzziness("AUTO")
                                    )
                            ),
                    ProductDocument.class // 결과 매핑할 클래스
            );

            // 결과 변환: SearchResponse -> List<ProductResponse>
            return response.hits().hits().stream()
                    .map(Hit::source) // Hit에서 ProductDocument 꺼내기
                    .map(doc -> ProductResponse.builder()
                            .id(Long.valueOf(doc.getId()))
                            .title(doc.getTitle())
                            .content(doc.getContent())
                            .price(doc.getPrice())
                            .quantity(doc.getQuantity())
                            .category(doc.getCategory())
                            .build())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Elasticsearch 검색 실패", e);
            throw new RuntimeException("검색 중 오류가 발생했습니다.");
        }
    }

}
