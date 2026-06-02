// src/main/java/org/likelion/domain/auth/dto/TokenResponse.java
package org.likelion.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}