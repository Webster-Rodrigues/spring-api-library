package io.github.websterrodrigues.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.driver-class-name}")
    String driver;

    //Hikari por padrão já vem com JPA
    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMaximumPoolSize(10); //Máximo de conexões
        config.setMinimumIdle(1); //Tamanho inicial do pool
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(600000); //Tempo máximo de vida da conexão 600 mil ms (10 min) -> Gerencia conexões
        config.setConnectionTimeout(100000); //Timeout de conexão
        config.setConnectionTestQuery("select 1"); //Teste de conexão

        return new HikariDataSource(config); //Hikari DataSource exige uma HikariConfig
    }

    //configuracao Hikary https://github.com/brettwooldridge/HikariCP
}


/*DataSource básico. Não é recomendado para produção. Ele só cria a conexão, não gerencia
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driver);
        return ds;
    }*/