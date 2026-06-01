package com.likelion.backend.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * ArticleService 클래스의 모든 메서드 실행 시간을 측정하는 AOP (4주차 필수 요구사항 2번)<br>
 */
@Slf4j
@Aspect
@Component
public class ArticleExecutionTimeAspect {

    // 필수 요구사항 2번
//    @Around("execution(* com.likelion.backend.api.article.service.ArticleService.*(..))")
    // 우대 요구사항 2번
    @Around("@annotation(com.likelion.backend.global.annotation.TrackExecutionTime)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName(); // 클래스 이름
        String methodName = signature.getName();    // 메서드 이름

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("[AOP] {}.{}() -> {}ms", className, methodName, stopWatch.getTotalTimeMillis());
        }
    }
}
