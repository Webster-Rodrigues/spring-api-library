package io.github.websterrodrigues.libraryapi.security;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

//Independente da autenticação, no final será produzido uma instância de CustomAuthentication porque ela carrega os dados do usuário autenticado
public class CustomAuthentication implements Authentication {


    private final SystemUser systemUser;

    public CustomAuthentication(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.systemUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        //SimpleGrantedAuthority -> Implementação simples de GrantedAuthority, que é um interface que retorna as authorities
    }

    @Override
    public Object getCredentials() {

        return systemUser.getPassword();
    }

    @Override
    public Object getDetails() {
        return systemUser;
    }

    @Override
    public Object getPrincipal() {
        return systemUser;
    }

    @Override
    public boolean isAuthenticated() {
        return true; //False -> Não autenticado
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return systemUser.getLogin();
    }
}
