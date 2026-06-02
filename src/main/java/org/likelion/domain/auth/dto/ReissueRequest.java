// src/main/java/org/likelion/domain/auth/dto/ReissueRequest.java
package org.likelion.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequest {

    @NotBlank
    private String refreshToken;
}