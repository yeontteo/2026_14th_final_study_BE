package com.likelion.backend.api.article.controller;

import com.likelion.backend.api.article.dto.request.ArticleCreateRequestDTO;
import com.likelion.backend.api.article.dto.request.ArticleUpdateRequestDTO;
import com.likelion.backend.api.article.dto.response.ArticleCreateResponseDTO;
import com.likelion.backend.api.article.dto.response.ArticleDetailResponseDTO;
import com.likelion.backend.api.article.service.ArticleService;
import com.likelion.backend.global.response.ApiResponse;
import com.likelion.backend.global.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "게시판(Article)", description = "게시판 관련 API 입니다.")
@RestController
@RequestMapping( "/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "게시글 생성 API", description = "게시글을 생성하는 API 입니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ArticleCreateResponseDTO>> createArticle(
            @RequestBody ArticleCreateRequestDTO req
    ) {
        ArticleCreateResponseDTO resp = articleService.createArticle(req);

        return ApiResponse.success(SuccessStatus.SUCCESS_ARTICLE_CREATE, resp);
    }

    @Operation(summary = "게시글 단건 조회 API", description = "게시글을 id로 단건 조회하는 API 입니다.")
    @GetMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponseDTO>> getArticleDetail(
            @PathVariable Long articleId
    ) {
        ArticleDetailResponseDTO resp = articleService.getArticleDetail(articleId);

        return ApiResponse.success(SuccessStatus.SUCCESS_ARTICLE_GET, resp);
    }

    @Operation(summary = "게시글 단건 수정 API", description = "게시글을 id로 단건 수정하는 API 입니다.")
    @PatchMapping("/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponseDTO>> updateArticle(
            @PathVariable Long articleId,
            @RequestBody ArticleUpdateRequestDTO req
    ) {
        ArticleDetailResponseDTO resp = articleService.updateArticle(articleId, req);

        return ApiResponse.success(SuccessStatus.SUCCESS_ARTICLE_UPDATE, resp);
    }

    @Operation(summary = "게시글 단건 삭제 API", description = "게시글 id로 단건 삭제하는 API 입니다.")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(
            @PathVariable Long articleId
    ) {
        articleService.deleteArticle(articleId);

        return ApiResponse.successOnly(SuccessStatus.SUCCESS_ARTICLE_DELETE);
    }
}
