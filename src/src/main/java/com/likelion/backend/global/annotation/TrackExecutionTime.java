package com.likelion.backend.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드 실행 시간을 ms 단위로 로그에 남기는 커스텀 어노테이션<br>
 * {@link com.likelion.backend.global.aop.ArticleExecutionTimeAspect}와 연동되어 동작
 *
 * <pre>
 * {@code
 * @TrackExecutionTime
 * public ArticleResponse getArticle(Long id) { ... }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackExecutionTime {
}
