package com.likelion.backend.api.article.dto.request;

/**
 * 게시글(Article) 단건 생성 요청 DTO
 * @param title 게시글 제목
 * @param content 게시글 내용
 * @param author 게시글 작성자
 */
public record ArticleCreateRequestDTO(

        String title,
        String content,
        String author
) {
}
