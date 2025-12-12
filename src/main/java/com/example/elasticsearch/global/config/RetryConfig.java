package com.example.elasticsearch.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry // 이 어노테이션이 핵심 (재시도 기능 활성화)
public class RetryConfig {
}