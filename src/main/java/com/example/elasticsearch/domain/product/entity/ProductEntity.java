package com.example.elasticsearch.domain.product.entity;

import com.example.elasticsearch.domain.product.entity.enumerate.Category;
import com.example.elasticsearch.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Integer price;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Category category;

    private ProductEntity(String title, String content, Integer price, Integer quantity, Category category) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public static ProductEntity of(String title, String content, Integer price, Integer quantity, Category category) {
        return new ProductEntity(title, content, price, quantity, category);
    }
}
