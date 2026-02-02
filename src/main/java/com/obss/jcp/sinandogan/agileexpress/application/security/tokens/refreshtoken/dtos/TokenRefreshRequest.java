package com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.dtos;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}

