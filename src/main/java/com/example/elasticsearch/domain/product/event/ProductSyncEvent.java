package com.example.elasticsearch.domain.product.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductSyncEvent {
    private String productId;
    private SyncOperation operation;

    public enum SyncOperation { CREATE, UPDATE, DELETE }
}