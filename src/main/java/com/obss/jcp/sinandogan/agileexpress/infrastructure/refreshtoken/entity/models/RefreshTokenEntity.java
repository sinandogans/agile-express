package com.obss.jcp.sinandogan.agileexpress.infrastructure.refreshtoken.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    public RefreshTokenEntity(String token, String email, Instant expirationDate) {
        this.token = token;
        this.email = email;
        this.expirationDate = expirationDate;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String token;
    private Instant expirationDate;
    private String email;
}
