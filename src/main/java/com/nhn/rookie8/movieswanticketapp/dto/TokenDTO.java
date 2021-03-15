package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private Access access;

    @Data
    public static class Access {
        private Token token;
    }

    @Data
    public static class Token {
        private LocalDateTime expires;
        private String id;
    }
}
