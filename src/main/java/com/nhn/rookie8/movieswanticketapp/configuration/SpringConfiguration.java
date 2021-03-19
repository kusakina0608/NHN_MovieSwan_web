package com.nhn.rookie8.movieswanticketapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhn.rookie8.movieswanticketapp.dto.SecretTicketDataDTO;
import com.nhn.rookie8.movieswanticketapp.dto.SecretKeyManagerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
@Order(value=1)
public class SpringConfiguration {

    @Value("${SKM.Url}")
    private String SKMurl;

    @Value("${SKM.appkey}")
    private String appKey;

    @Value("${SKM.keyid}")
    private String keyId;

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
    public SecretTicketDataDTO databaseInfoDTO() throws Exception{

        RestTemplate restTemplate = restTemplate();
        ObjectMapper objectMapper = objectMapper();

        SecretKeyManagerDTO secretKeyManagerDTO =
                restTemplate.getForObject(SKMurl.replace("{appkey}",appKey).replace("{keyid}",keyId)
                        , SecretKeyManagerDTO.class);

        return objectMapper.readValue(secretKeyManagerDTO.getBody().getSecret(), SecretTicketDataDTO.class);
    }

}
