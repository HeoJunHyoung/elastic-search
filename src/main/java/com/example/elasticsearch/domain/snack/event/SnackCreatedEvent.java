package com.example.elasticsearch.domain.snack.event;

import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SnackCreatedEvent {
    private final SnackEntity snackEntity;
}