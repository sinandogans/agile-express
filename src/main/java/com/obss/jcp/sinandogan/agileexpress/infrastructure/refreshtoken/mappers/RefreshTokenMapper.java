package com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.mappers;

import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.RefreshToken;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.entity.models.RefreshTokenEntity;

public class RefreshTokenMapper {
    public static RefreshToken toDomain(RefreshTokenEntity entity) {
        if (entity == null) return null;
        return new RefreshToken(entity.getId(), entity.getToken(), entity.getEmail(), entity.getExpirationDate());
    }

    public static RefreshTokenEntity toEntity(RefreshToken domain) {
        if (domain == null) return null;
        return new RefreshTokenEntity(domain.getId(), domain.getToken(), domain.getExpirationDate(), domain.getEmail());
    }
}
