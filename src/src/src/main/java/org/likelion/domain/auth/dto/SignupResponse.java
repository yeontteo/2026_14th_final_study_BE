// src/main/java/org/likelion/domain/auth/dto/SignupResponse.java
package org.likelion.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.likelion.domain.member.entity.Member;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long id;
    private String name;
    private String email;

    public static SignupResponse from(Member member) {
        return new SignupResponse(member.getId(), member.getName(), member.getEmail());
    }
}