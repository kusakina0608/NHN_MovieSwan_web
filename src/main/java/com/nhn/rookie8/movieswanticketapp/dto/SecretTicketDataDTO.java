package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecretTicketDataDTO {

    private ObjectStorage objectStorage;
    private Redis redis;
    private Database database;
    private String secretKey;

    @Data
    public static class ObjectStorage {
        private String password;
        private String username;
        private String tenantId;
    }

    @Data
    public static class Redis {
        private String password;
    }

    @Data
    public static class Database {
        private String password;
        private String username;
    }
}


