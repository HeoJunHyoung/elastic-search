package com.example.elasticsearch.domain.snack.dto.request;

import com.example.elasticsearch.domain.snack.entity.SnackEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SnackRequest {
    private String name;
    private String description;
    private int price;
    private String brand;

    public SnackEntity toEntity() {
        return SnackEntity.builder()
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .brand(this.brand)
                .build();
    }
}