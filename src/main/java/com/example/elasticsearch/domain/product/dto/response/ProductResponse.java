package com.example.elasticsearch.domain.product.dto.response;

import com.example.elasticsearch.domain.product.entity.enumerate.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private final Long id;

    private final String title;

    private final String content;

    private final Integer price;

    private final Integer quantity;

    private final Category category; // SMARTPHONE, LAPTOP, DESKTOP, TABLET

}
