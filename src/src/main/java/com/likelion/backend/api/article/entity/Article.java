package com.likelion.backend.api.article.entity;

import com.likelion.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 엔티티 클래스
 */
@Entity
@Getter
@Table(name = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 게시글 ID (PK)

    @Column(nullable = false)
    private String title;       // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;     // 게시글 내용

    @Column(nullable = false)
    private String author;      // 게시글 작성자

    @Builder
    private Article(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static Article create(String title, String content, String author) {
        return Article.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
