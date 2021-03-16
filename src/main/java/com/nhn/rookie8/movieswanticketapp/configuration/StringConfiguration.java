package com.nhn.rookie8.movieswanticketapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhn.rookie8.movieswanticketapp.dto.SecretDataDTO;
import com.nhn.rookie8.movieswanticketapp.dto.SecretKeyManagerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@Import(DataSourceConfiguration.class)
public class StringConfiguration {

    @Value("${SKMUrl}")
    private String url;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    @DependsOn({"restTemplate","objectMapper"})
    public SecretDataDTO databaseInfoDTO() throws Exception{

        RestTemplate restTemplate = restTemplate();
        ObjectMapper objectMapper = objectMapper();

        SecretKeyManagerDTO secretKeyManagerDTO = restTemplate.getForObject(url, SecretKeyManagerDTO.class);

        return objectMapper.readValue(secretKeyManagerDTO.getBody().getSecret(),SecretDataDTO.class);
    }

}
