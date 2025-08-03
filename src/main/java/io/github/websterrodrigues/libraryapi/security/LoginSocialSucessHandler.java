package io.github.websterrodrigues.libraryapi.security;

import io.github.websterrodrigues.libraryapi.exceptions.EntityNotFoundException;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class LoginSocialSucessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SystemUserService systemUserService;
    private final static  String SENHA_PADRAO = "123";

    public LoginSocialSucessHandler(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2AuthenticationToken.getPrincipal();

        //Proteção caso o provedor OAuth2 não forneça o email do usuário
        String email = Objects.requireNonNull(oauth2User.getAttribute("email"), "Email não fornecido pelo provedor OAuth2");
        SystemUser systemUser;

        try {
            systemUser = systemUserService.findByEmail(email);

        }
        catch (EntityNotFoundException exception){
             registerSystemUser(email);
             systemUser = systemUserService.findByEmail(email);
        }

        authentication = new CustomAuthentication(systemUser);

        SecurityContextHolder.getContext().setAuthentication(authentication);//Garante que qualquer chamada

        super.onAuthenticationSuccess(request, response, authentication);

    }

    private void registerSystemUser(String email) {
        SystemUser systemUserSave = new SystemUser();
        systemUserSave.setLogin(loginBuilder(email));
        systemUserSave.setEmail(email);
        systemUserSave.setPassword(SENHA_PADRAO);
        systemUserSave.getRoles().add("USER");
        systemUserService.save(systemUserSave);
    }

    private String loginBuilder(String email){
        return email.substring(0, email.indexOf("@"));
    }


}
