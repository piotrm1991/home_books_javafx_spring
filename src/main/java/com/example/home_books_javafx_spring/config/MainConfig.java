package com.example.home_books_javafx_spring.config;

import com.example.home_books_javafx_spring.util.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class MainConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    @Profile("dev")
    public DataSource getDataSourceH2() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:h2:mem:mydb");
        dataSourceBuilder.username("");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }

    @Bean
    @Profile("prod")
    public DataSource getDataSourceLocal() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/home_books?useSSL=false&amp&serverTimezone=UTC");
        dataSourceBuilder.username("skstudent");
        dataSourceBuilder.password("skstudent");
        return dataSourceBuilder.build();
    }

    @Bean
    public EntityValidator entityValidator(ApplicationContext applicationContext) {
        return new EntityValidator(applicationContext);
    }
}
