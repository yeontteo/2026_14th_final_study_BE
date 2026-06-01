package com.likelion.backend.api.article.service;

import com.likelion.backend.api.article.dto.request.ArticleCreateRequestDTO;
import com.likelion.backend.api.article.dto.request.ArticleUpdateRequestDTO;
import com.likelion.backend.api.article.dto.response.ArticleCreateResponseDTO;
import com.likelion.backend.api.article.dto.response.ArticleDetailResponseDTO;
import com.likelion.backend.api.article.entity.Article;
import com.likelion.backend.api.article.repository.ArticleRepository;
import com.likelion.backend.global.annotation.TrackExecutionTime;
import com.likelion.backend.global.exception.ArticleNotFoundException;
import com.likelion.backend.global.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * 게시글 단건 생성
     *
     * @param req 생성할 게시글의 제목, 내용, 작성자
     * @return 생성된 게시글의 id, 제목, 내용, 작성자, 생성 시각
     */
    @Transactional
    @TrackExecutionTime
    public ArticleCreateResponseDTO createArticle(ArticleCreateRequestDTO req) {

        Article article = Article.create(
                req.title(),
                req.content(),
                req.author()
        );

        return ArticleCreateResponseDTO.from(articleRepository.save(article));
    }

    /**
     * 게시글 단건 상세 조회
     * @param articleId 조회할 게시글 id
     * @return 조회된 게시글의 id, 제목, 내용, 작성자, 생성 시각, 수정 시각
     * @throws ArticleNotFoundException 해당 id의 게시글이 존재하지 않는 경우 발생
     */
    public ArticleDetailResponseDTO getArticleDetail(Long articleId) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(ErrorStatus.NOT_FOUND_ARTICLE));

        return ArticleDetailResponseDTO.from(article);
    }

    /**
     * 게시글 단건 수정
     * @param articleId 수정할 게시글 id
     * @param req 수정할 제목, 내용
     * @return 수정된 게시글의 id, 제목, 내용, 작성자, 생성 시각, 수정 시각
     * @throws ArticleNotFoundException 해당 id의 게시글이 존재하지 않는 경우
     */
    @Transactional
    public ArticleDetailResponseDTO updateArticle(Long articleId, ArticleUpdateRequestDTO req) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(ErrorStatus.NOT_FOUND_ARTICLE));

        article.update(req.title(), req.content());

        return ArticleDetailResponseDTO.from(article);
    }

    /**
     * 게시글 단건 삭제
     * @param articleId 삭제할 게시글 id
     * @throws ArticleNotFoundException 해당 id의 게시글이 존재하지 않는 경우 발생
     */
    @Transactional
    public void deleteArticle(Long articleId) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(ErrorStatus.NOT_FOUND_ARTICLE));

        articleRepository.delete(article);
    }
}
