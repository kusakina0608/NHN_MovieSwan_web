package com.nhn.rookie8.movieswanticketapp.configuration;

import com.nhn.rookie8.movieswanticketapp.dto.SecretTicketDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Order(value=4)
public class DataSourceConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    private final SecretTicketDataDTO secretTicketDataDTO;

    @Bean
    public DataSource getDataSource(){
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(secretTicketDataDTO.getDatabase().getUsername())
                .password(secretTicketDataDTO.getDatabase().getPassword())
                .build();
    }
}
