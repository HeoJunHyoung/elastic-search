package com.example.elasticsearch.domain.snack.repository;

import com.example.elasticsearch.domain.snack.document.SnackDocument;

import java.util.List;

public interface SnackSearchRepositoryCustom {
    List<SnackDocument> searchByKeyword(String keyword);
}
