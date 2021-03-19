package com.nhn.rookie8.movieswanticketapp;

import com.nhn.rookie8.movieswanticketapp.configuration.ExternalLoginConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(ExternalLoginConfiguration.class)
public class MovieswanTicketAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieswanTicketAppApplication.class, args);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10000000);
		return multipartResolver;
	}
}
