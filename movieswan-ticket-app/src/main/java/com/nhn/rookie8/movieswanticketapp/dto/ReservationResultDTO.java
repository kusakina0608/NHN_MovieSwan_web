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
public class ReservationResultDTO {
    private String rid;
    private String tid;
    private String uid;
    private String mid;
    private String movieName;
    private int childNum;
    private int adultNum;
    private int oldNum;
    private int totalNum;
    private int price;
    private LocalDateTime startDate;
    private LocalDateTime regDate;
}
