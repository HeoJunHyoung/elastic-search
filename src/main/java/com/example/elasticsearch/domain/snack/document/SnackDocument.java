package com.example.elasticsearch.domain.snack.document;

import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "snacks")
@Setting(settingPath = "/elastic/snack-setting.json")
public class SnackDocument {

    @Id
    private String id; // Elasticsearch ID는 문자열로 관리하는 것이 일반적

    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Keyword) // 정확한 일치 검색(필터링)용
    private String brand;

    // 추후 리뷰 평점을 집계해서 넣을 필드 (초기값은 0.0)
    @Field(type = FieldType.Double)
    private double avgRating;

    // Entity -> Document 변환 편의 메서드
    public static SnackDocument from(SnackEntity entity) {
        return SnackDocument.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .brand(entity.getBrand())
                .avgRating(0.0) // 초기 생성 시 평점은 0
                .build();
    }
}