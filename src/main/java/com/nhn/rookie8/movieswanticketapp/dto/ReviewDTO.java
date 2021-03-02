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
    private String reviewId;
    private String movieId;
    private String memberId;
    private int rating;
    private String content;
    private LocalDateTime regDate, modDate;
}
