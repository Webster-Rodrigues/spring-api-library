package io.github.websterrodrigues.libraryapi.config;

import io.github.websterrodrigues.libraryapi.security.CustomUserDetailsService;
import io.github.websterrodrigues.libraryapi.security.LoginSocialSucessHandler;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)//Permite definir regras de autorização diretamente nos métodos das classes
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSucessHandler sucessHandler) throws Exception{
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
                .build();
    };

    // PasswordEncoder é responsável por criptografar as senhas dos usuários
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10); //Uma vez que a senha foi criptografada com BCrypt, não é possível descriptografá-la.
        //strength 10 diz quantas vezes o algoritmo vai rodar para gerar o hash da senha.
    }


    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }


    //@Bean
    public UserDetailsService userDetailsService(SystemUserService systemUserService) {
        return new CustomUserDetailsService(systemUserService);
    }

}
//Exemplos de config de roles
//authorize.requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN");
//authorize.requestMatchers(HttpMethod.POST, "/authors/**").hasRole("ADMIN");
//authorize.requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN");
//authorize.requestMatchers(HttpMethod.GET, "/authors/**").hasAnyRole("USER", "ADMIN");
