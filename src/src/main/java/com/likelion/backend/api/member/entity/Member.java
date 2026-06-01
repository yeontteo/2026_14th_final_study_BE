package com.likelion.backend.api.member.entity;

import com.likelion.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                        // 회원 ID (PK)

    @Column(nullable = false, unique = true, length = 100)
    private String email;                   // 이메일

    @Column(nullable = false, length = 255)
    private String password;                // 비밀번호

    @Column(nullable = false, length = 20)
    private String name;                    // 본명

    @Column(nullable = false, length = 20)
    private String department;              // 학과

    @Column(nullable = false)
    private boolean isDeleted;              // 삭제 여부

    private LocalDateTime deletedAt;        // 삭제 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;                      // 권한
}
