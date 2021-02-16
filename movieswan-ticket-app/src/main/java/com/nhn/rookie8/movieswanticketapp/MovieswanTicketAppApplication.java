package com.nhn.rookie8.movieswanticketapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MovieswanTicketAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieswanTicketAppApplication.class, args);
	}

}
