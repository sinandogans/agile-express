package com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    public RefreshToken(String token, String email, Instant expirationDate) {
        this.email = email;
        this.expirationDate = expirationDate;
        this.token = token;
    }
    private UUID id;
    private String token;
    private String email;
    private Instant expirationDate;
}
