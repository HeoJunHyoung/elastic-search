package com.example.elasticsearch.domain.product.document;

import com.example.elasticsearch.domain.product.entity.ProductEntity;
import com.example.elasticsearch.domain.product.entity.enumerate.Category;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Document(indexName = "products")
public class ProductDocument {

    @Id @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Integer)
    private Integer quantity;

    @Field(type = FieldType.Keyword)
    private Category category;

    public static ProductDocument from(ProductEntity entity) {
        return ProductDocument.builder()
                .id(entity.getId().toString())
                .title(entity.getTitle())
                .content(entity.getContent())
                .price(entity.getPrice())
                .quantity(entity.getQuantity())
                .category(entity.getCategory())
                .build();
    }

}
