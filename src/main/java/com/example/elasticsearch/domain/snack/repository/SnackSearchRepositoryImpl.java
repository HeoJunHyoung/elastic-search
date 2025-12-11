package com.example.elasticsearch.domain.snack.repository;

import com.example.elasticsearch.domain.snack.document.SnackDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SnackSearchRepositoryImpl implements SnackSearchRepositoryCustom {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<SnackDocument> searchByKeyword(String keyword) {

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("name")
                                .query(keyword)
                                // fuzziness: 오타가 있어도 찾고 싶다면 유지, 정확히 입력한 접두어만 찾고 싶다면 제거
                                .fuzziness("AUTO")
                        )
                )
                .build();

        SearchHits<SnackDocument> searchHits = elasticsearchOperations.search(query, SnackDocument.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}