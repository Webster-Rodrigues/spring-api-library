package io.github.websterrodrigues.libraryapi.security;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//Responsável por buscar dados do usuário
public class CustomUserDetailsService implements UserDetailsService {

    private final SystemUserService service;

    public CustomUserDetailsService(SystemUserService service){
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        SystemUser systemUser = service.findByLogin(login);

        return User.builder()
                .username(systemUser.getLogin())
                .password(systemUser.getPassword())
                .roles(systemUser.getRoles().toArray(new String[systemUser.getRoles().size()]))
                .build();
    }
}
