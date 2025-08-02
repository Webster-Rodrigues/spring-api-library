package io.github.websterrodrigues.libraryapi.security;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SystemUserService service;

    @Autowired
    private PasswordEncoder encoder;

    //Método responsável por autenticar o usuário
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        SystemUser obj = service.findByLogin(login);

        if (obj == null){
            throw getUserNotFound();
        }

        String passwordEncoded = obj.getPassword();

        boolean isPasswordValid = encoder.matches(password, passwordEncoded);

        if (isPasswordValid){
            return new CustomAuthentication(obj);
        }

        throw getUserNotFound();
    }

    private UsernameNotFoundException getUserNotFound() {
        return new UsernameNotFoundException("Usuário e/ou senha incorretos!");
    }

    //Indica quais tipos de autenticação esse provider suporta
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
        // Verifica se o tipo de autenticação é compatível com UsernamePasswordAuthenticationToken. Que é o tipo de autenticação usado para login com nome de usuário e senha.
    }
}
