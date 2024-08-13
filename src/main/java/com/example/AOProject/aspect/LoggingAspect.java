package com.example.AOProject.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.example.AOProject.service.UserService.*(..)) || " +
            "execution(* com.example.AOProject.service.OrderService.*(..))")
    public void loggingPointcut() {}

    @Around("loggingPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Выполнение метода {} с аргументами {}", methodName, args);

        Object result = joinPoint.proceed(args);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Метод {} выполнился за {} мс с результатом {}", methodName, duration, result);

        return result;
    }
}
