package io.github.websterrodrigues.libraryapi.aspects;


import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.security.SecurityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private SecurityService securityService;

    private final static Logger logger = LoggerFactory.getLogger(LoggingAspect .class);

    @Pointcut("execution(* io.github.websterrodrigues.libraryapi.service.AuthorService.*(..))")
    public void authorServiceMethods(){}

    @Around("authorServiceMethods()")
    public Object LogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("[START] Iniciando chamada [{}.{}] com argumentos [{}}] ",
                joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName(), joinPoint.getArgs());

        Object obj = joinPoint.proceed();
        SystemUser user = securityService.getAuthenticatedUser();

        logger.info("[AUDITORIA] Responsável pela alteração [{}] ROLES: [{}]", user.getLogin(), user.getRoles());
        logger.info("[END] Chamada [{}.{}] finalizado!", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        return obj;
    }

}
