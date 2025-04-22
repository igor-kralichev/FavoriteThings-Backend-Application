package com.example.favoritethings.backend.util;

import com.example.favoritethings.backend.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggerAspect {

    @Autowired
    private LogService logService;

    // Входящий запрос
    @Before("within(com.example.favoritethings.backend.controller..*)")
    public void logBefore(JoinPoint joinPoint) {
        String message = "[" + LocalDateTime.now() + "] Входящий запрос: " + joinPoint.getSignature();
        System.out.println(message);
        logService.log("REQUEST_IN", message);
    }

    // Успешный выход
    @AfterReturning(pointcut = "within(com.example.favoritethings.backend.controller..*)", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String message = "[" + LocalDateTime.now() + "] Запрос завершён: " + joinPoint.getSignature();
        System.out.println(message);
        logService.log("REQUEST_OUT", message);
    }

    // Исключение
    @AfterThrowing(pointcut = "within(com.example.favoritethings.backend.controller..*)", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String message = "[" + LocalDateTime.now() + "] Ошибка в методе " + joinPoint.getSignature() + ": " + ex.getMessage();
        System.err.println(message);
        logService.log("REQUEST_ERROR", message);
    }

    // Время выполнения метода
    @Around("within(com.example.favoritethings.backend.controller..*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed(); // запускаем метод
        } catch (Throwable ex) {
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            String message = "[" + LocalDateTime.now() + "] Время выполнения " + joinPoint.getSignature() + ": " + duration + " мс";
            System.out.println(message);
            logService.log("REQUEST_TIME", message);
        }
        return result;
    }
}
