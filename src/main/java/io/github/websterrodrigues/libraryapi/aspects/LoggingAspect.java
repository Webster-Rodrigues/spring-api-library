package io.github.websterrodrigues.libraryapi.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* io.github.websterrodrigues.libraryapi.service.AuthorService.*(..))")
    public Object LogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Antes [TESTE] " + joinPoint.getSignature());

        Object obj = joinPoint.proceed();

        System.out.println("Depois [TESTE] " + joinPoint.getSignature());
        return obj;
    }

}
