package com.example.elasticsearch.domain.product.dto.request;

import com.example.elasticsearch.domain.product.entity.enumerate.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private String title;

    private String content;

    private Integer price;

    private Integer quantity;

    private Category category; // SMARTPHONE, LAPTOP, DESKTOP, TABLET

}
