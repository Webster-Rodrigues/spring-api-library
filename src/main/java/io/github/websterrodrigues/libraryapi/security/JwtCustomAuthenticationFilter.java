package io.github.websterrodrigues.libraryapi.security;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    private final SystemUserService service;

    public JwtCustomAuthenticationFilter(SystemUserService service) {
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isconverted(authentication)){
            String login = authentication.getName();
            SystemUser systemUser = service.findByLogin(login);

            authentication = new CustomAuthentication(systemUser);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private  boolean isconverted(Authentication authentication){
        return authentication != null && authentication instanceof JwtAuthenticationToken;
    }
}
