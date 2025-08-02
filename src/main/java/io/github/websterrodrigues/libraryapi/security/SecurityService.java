package io.github.websterrodrigues.libraryapi.security;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    @Autowired
    private SystemUserService service;


    public SystemUser getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String login = user.getUsername();
        return service.findByLogin(login);
    }
}
