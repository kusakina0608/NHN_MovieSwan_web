package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDTO {

    private Integer httpCode;

    private boolean error;

    private String message;

    private UserDTO content;
}
