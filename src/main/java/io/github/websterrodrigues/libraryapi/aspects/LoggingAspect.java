package io.github.websterrodrigues.libraryapi.aspects;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.security.SecurityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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

    @Pointcut("within(io.github.websterrodrigues.libraryapi.service.BookService) || " +
            "within(io.github.websterrodrigues.libraryapi.service.AuthorService)")
    public void BookAndAuthorService(){}

    @Pointcut("BookAndAuthorService() && execution(* findById(..))")
    public void findByIdMethods(){}

    @Pointcut("BookAndAuthorService() && execution(* save(..))")
    public void saveEntityMethods(){}

    @Pointcut("BookAndAuthorService() && execution(* update(..))")
    public void updateEntityMethods(){}

    @Pointcut("BookAndAuthorService() && execution(* delete(..))")
    public void deleteEntityMethods(){}

    @Around("findByIdMethods()")
    public Object logAroundFindById(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        logger.debug("[START] Iniciando chamada [{}.{}]", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        logger.info("[METHOD] {} | Buscando {} ID {} no repository", joinPoint.getSignature().getName(), signature.getReturnType().getSimpleName(), joinPoint.getArgs());

        Object obj = joinPoint.proceed();
        SystemUser user = securityService.getAuthenticatedUser();

        logger.info("[AUDIT] Responsável pela busca [{}] ROLES: [{}]", user.getLogin(), user.getRoles());
        logger.info("[RETURN] {} ID: {} encontrado com sucesso!", obj.getClass().getSimpleName(),joinPoint.getArgs());
        logger.debug("[END] Chamada [{}.{}] finalizada!", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        return obj;
    }

    @Around("saveEntityMethods()")
    public Object logAroundSave(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        logger.debug("[START] Iniciando chamada [{}.{}]", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        logger.info("[METHOD] {} | Salvando {} no repository", joinPoint.getSignature().getName(), signature.getReturnType().getSimpleName());

        Object obj = joinPoint.proceed();
        SystemUser user = securityService.getAuthenticatedUser();

        logger.info("[AUDIT] Responsável pelo cadastro [{}] ROLES: [{}]", user.getLogin(), user.getRoles());
        logger.debug("[RETURN] {}", obj);
        logger.info("[RETURN] {} cadastrado com sucesso!", obj.getClass().getSimpleName(),joinPoint.getArgs());
        logger.debug("[END] Chamada [{}.{}] finalizada!", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        return obj;
    }

    @Around("updateEntityMethods()")
    public Object logAroundUpdate(ProceedingJoinPoint joinPoint) throws Throwable {

        logger.debug("[START] Iniciando chamada [{}.{}]", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        logger.info("[METHOD] {} | Atualizando {} ID {} no repository", joinPoint.getSignature().getName(), joinPoint.getArgs()[0].getClass().getSimpleName(),joinPoint.getArgs()[0].getClass().getMethod("getId").invoke(joinPoint.getArgs()[0]));

        Object obj = joinPoint.proceed();
        SystemUser user = securityService.getAuthenticatedUser();

        logger.info("[AUDIT] Responsável pela modificação [{}] ROLES: [{}]", user.getLogin(), user.getRoles());
        logger.debug("[RETURN] {}", joinPoint.getArgs());
        logger.info("[RETURN] {} ID: {} atualizado com sucesso!", joinPoint.getArgs()[0].getClass().getSimpleName(), joinPoint.getArgs()[0].getClass().getMethod("getId").invoke(joinPoint.getArgs()[0]));
        logger.debug("[END] Chamada [{}.{}] finalizada!", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        return obj;
    }

    @Around("deleteEntityMethods()")
    public Object logAroundDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        logger.debug("[START] Iniciando chamada [{}.{}]", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        logger.info("[METHOD] {} | Deletando {} ID {} no repository", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringType().getSimpleName().replace("Service",""), joinPoint.getArgs()[0]);

        Object obj = joinPoint.proceed();
        SystemUser user = securityService.getAuthenticatedUser();

        logger.info("[AUDIT] Responsável pela deleção [{}] ROLES: [{}]", user.getLogin(), user.getRoles());
        logger.info("[RETURN] {} ID: {} deletado com sucesso!", joinPoint.getSignature().getDeclaringType().getSimpleName().replace("Service",""), joinPoint.getArgs()[0]);
        logger.debug("[END] Chamada [{}.{}] finalizada!", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
        return obj;
    }

}
