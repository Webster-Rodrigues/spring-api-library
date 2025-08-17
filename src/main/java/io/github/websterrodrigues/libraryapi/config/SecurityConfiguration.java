package io.github.websterrodrigues.libraryapi.config;

import io.github.websterrodrigues.libraryapi.security.JwtCustomAuthenticationFilter;
import io.github.websterrodrigues.libraryapi.security.LoginSocialSucessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)//Permite definir regras de autorização diretamente nos métodos das classes
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LoginSocialSucessHandler sucessHandler,
                                                   JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception{
        return http
                // Desabilita a proteção CSRF. O spring habilita a proteção CSRF para métodos HTTP que alteram estado
                // (POST, PUT, DELETE, PATCH). Ele faz isso exigindo um token CSRF em cada requisição que modifica dados
                .csrf(AbstractHttpConfigurer :: disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(HttpMethod.POST, "/users/**").permitAll(); //Permite acesso a todos os endpoints de usuários, sem autenticação


                    authorize.anyRequest().authenticated();// Exige autenticação para todas as requisições
                })
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(sucessHandler);
                })
                .oauth2ResourceServer(oauth2Rs -> oauth2Rs.jwt(Customizer.withDefaults()))//Habilita o Resource Server para validar tokens JWT
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();
    };


    //Configura o prefixo das roles.
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    //Configura, no token JWT, o prefixo scope
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

}
//Exemplos de config de roles
//authorize.requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN");
//authorize.requestMatchers(HttpMethod.POST, "/authors/**").hasRole("ADMIN");
//authorize.requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN");
//authorize.requestMatchers(HttpMethod.GET, "/authors/**").hasAnyRole("USER", "ADMIN");
