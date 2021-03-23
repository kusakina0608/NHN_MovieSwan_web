package com.nhn.rookie8.movieswanticketapp.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Map;


@Configuration
@Order(value=6)
@Data
@ConfigurationProperties(prefix = "external.login")
public class ExternalLoginConfiguration{

    private Map<String, String> url;
}
