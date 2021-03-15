package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailDTO {
    private String reservationId;
    private String memberId;
    private String poster;
    private String title;
    private int youngNum;
    private int adultNum;
    private int elderNum;
    private int totalNum;
    private int price;
    private List<String> seats;
    private LocalDateTime startTime;
    private LocalDateTime payDate;
}
