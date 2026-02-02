package com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    public String accessToken;
    public String refreshToken;
}
