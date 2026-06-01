package com.likelion.backend.api.article.dto.response;

import com.likelion.backend.api.article.entity.Article;

import java.time.LocalDateTime;

/**
 * 게시글(Article) 상세 조회 응답 DTO
 * @param id 게시글 ID
 * @param title 게시글 제목
 * @param content 게시글 내용
 * @param author 게시글 작성자
 * @param createdAt 게시글 생성 시각
 * @param updatedAt 게시글 수정 시각
 */
public record ArticleDetailResponseDTO(
    Long id,
    String title,
    String content,
    String author,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ArticleDetailResponseDTO from(Article article) {
        return new ArticleDetailResponseDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
