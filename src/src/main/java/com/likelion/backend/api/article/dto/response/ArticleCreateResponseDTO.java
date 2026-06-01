package com.likelion.backend.api.article.dto.response;

import com.likelion.backend.api.article.entity.Article;

import java.time.LocalDateTime;

/**
 * 게시글(Article) 단건 생성 응답 DTO
 * @param id 게시글 ID
 * @param title 게시글 제목
 * @param content 게시글 내용
 * @param author 게시글 작성자
 * @param createdAt 게시글 생성 시각
 */
public record ArticleCreateResponseDTO(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public static ArticleCreateResponseDTO from(Article article) {
        return new ArticleCreateResponseDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor(),
                article.getCreatedAt()
        );
    }
}
