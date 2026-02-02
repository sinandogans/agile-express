package com.obss.jcp.sinandogan.agileexpress.application.services.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class RefreshDto {
    private String accessToken;
}
