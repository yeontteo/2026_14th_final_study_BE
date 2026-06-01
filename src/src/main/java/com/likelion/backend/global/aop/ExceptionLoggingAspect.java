package com.likelion.backend.global.aop;

import com.likelion.backend.global.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @Pointcut("execution(* com.likelion.backend.api..service..*(..))")
    private void applicationLayer() {}

    @AfterThrowing(
            pointcut = "applicationLayer()",
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        if (ex instanceof BaseException baseEx) {

            log.warn("[Exception] {}.{}() | args={} | {} - {}",
                    className,
                    methodName,
                    args,
                    baseEx.getClass().getSimpleName(),
                    baseEx.getMessage()
            );
        } else {
            log.error("[Exception] {}.{}() | args={} | {} - {}",
                    className,
                    methodName,
                    args,
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
                    ex
            );
        }
    }
}
