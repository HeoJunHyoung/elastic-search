package com.example.elasticsearch.domain.snack.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SnackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 과자 이름

    @Column(length = 1000)
    private String description; // 상세 설명

    @Column(nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private String brand; // 브랜드 (예: 농심, 오리온)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 등록일
}