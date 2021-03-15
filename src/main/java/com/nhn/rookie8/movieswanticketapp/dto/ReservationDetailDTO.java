package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public String toSimpleString() {
        return reservationId + "\t"
                + title + "\t"
                + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\t"
                + payDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String toDetailedString() {
        return "예약 번호 : " + reservationId + "\n"
                + "영화 제목 : " + title + "\n"
                + "시작 시간 : " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n"
                + "예약 일자 : " + payDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n"
                + "좌석 정보 : " + seats.toString() + "\n"
                + "결제 정보\n"
                + "성인 : " + adultNum + "\t"
                + "청소년 : " + youngNum + "\t"
                + "우대 : " + elderNum + "\n"
                + "총 결제 금액 : " + price;
    }
}
