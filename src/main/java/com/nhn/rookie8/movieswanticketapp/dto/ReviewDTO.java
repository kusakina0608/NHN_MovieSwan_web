package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDTO {
    private String rid;
    private String mid;
    private String uid;
    private int grade;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime moddate;
}
