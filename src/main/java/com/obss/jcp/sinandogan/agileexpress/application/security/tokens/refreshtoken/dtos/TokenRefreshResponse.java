package com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.dtos;

import lombok.Data;

@Data
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
}
