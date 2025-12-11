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
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "snacks")
@Setting(settingPath = "/elastic/snack-setting.json")
public class SnackDocument {

    @Id
    private String id;

    // [핵심 수정]
    // analyzer = "nori_edge_ngram": 저장 시 '신당동' -> '신', '신당', '신당동'으로 쪼개서 저장
    // searchAnalyzer = "nori": 검색 시 '신' -> '신'으로 분석하여 매칭 시도
    @Field(type = FieldType.Text, analyzer = "nori_edge_ngram", searchAnalyzer = "nori")
    private String name;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String description;

    @Field(type = FieldType.Integer)
    private int price;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Double)
    private double avgRating;

    public static SnackDocument from(SnackEntity entity) {
        return SnackDocument.builder()
                .id(entity.getId().toString())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .brand(entity.getBrand())
                .avgRating(0.0)
                .build();
    }
}