package io.github.websterrodrigues.libraryapi.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {

        var serverConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

        http.securityMatcher(serverConfigurer.getEndpointsMatcher())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .with(
                        serverConfigurer,
                        authorizationServer -> authorizationServer.oidc(Customizer.withDefaults())
                )
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()))
                .exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                ));

        return http.build();
    }

//    @Bean APRENDERRRRRR
//    @Order(1)
//    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
//
//        //Cria o configurer do Authorization Server. Faz a aplicacão se comportar como um Authorization Server
//        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
//
//        //Tratar apenas os endpoints do Authorization Server
//        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
//                .with(authorizationServerConfigurer, Customizer.withDefaults()); //Aplica o configurer ao HttpSecurity
//
//        authorizationServerConfigurer.oidc(Customizer.withDefaults());
//
//        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
//        http.formLogin(Customizer.withDefaults());
//
//        return http.build();
//    }


    //Responsável por definir as configurações do servidor de autorização
    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .build();
    }

    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)//Solicita consentimento do usuário para autorizar o cliente. Com false, basta se autenticar
                .build();
    }

    // PasswordEncoder é responsável por criptografar as senhas dos usuários
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10); //Uma vez que a senha foi criptografada com BCrypt, não é possível descriptografá-la.
        //strength 10 diz quantas vezes o algoritmo vai rodar para gerar o hash da senha.
    }

    // JWKSource é responsável por fornecer as chaves públicas para validar os tokens JWT
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws  Exception{
        RSAKey rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);

    }

    //Gerar par de chaves RSA para assinar os tokens JWT
    private RSAKey generateRsaKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Tamanho da chave RSA
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
}
