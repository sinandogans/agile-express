package com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email, long refreshTokenDurationMs);

    void verifyExpiration(RefreshToken token);

    RefreshToken findByToken(String token);

    String refreshAccessToken(String refreshToken);

    void deleteByEmail(String email);
}
