package com.likelion.backend.api.article.dto.request;

public record ArticleUpdateRequestDTO(
        String title,
        String content
) {
}
