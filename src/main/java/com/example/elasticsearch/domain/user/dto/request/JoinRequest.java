package com.example.elasticsearch.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
    private String username;
    private String password;
}