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
    private String reservationId;
    private String timetableId;
    private String memberId;
    private int youngNum;
    private int adultNum;
    private int elderNum;
    private int totalNum;
    private int price;
    private LocalDateTime payDate, regDate, modDate;
}
