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
public class ReservationDTO {
    private String rid;
    private String tid;
    private String uid;
    private int childNum;
    private int adultNum;
    private int oldNum;
    private int totalNum;
    private int price;
    private LocalDateTime regDate;
}
