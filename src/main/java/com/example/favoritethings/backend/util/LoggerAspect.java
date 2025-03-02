package com.example.favoritethings.backend.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Аспект для логирования вызовов REST контроллеров.
 * Логи выводятся в консоль и могут сохраняться в БД.
 */
@Aspect
@Component
public class LoggerAspect {

    @Before("within(com.example.favoritethings.controller..*)")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[" + LocalDateTime.now() + "] Входящий запрос: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "within(com.example.favoritethings.controller..*)", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("[" + LocalDateTime.now() + "] Запрос завершён: " + joinPoint.getSignature());
    }
}
