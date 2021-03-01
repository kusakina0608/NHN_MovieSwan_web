package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableDTO {
    private String timetableId;
    private String movieId;
    private LocalDateTime startTime;
    private LocalDateTime regDate, modDate;
    public String getScheduleString(){
        return startTime.getYear() + "년 "
                + startTime.getMonthValue() + "월 "
                + startTime.getDayOfMonth() + "일 "
                + startTime.getHour() + "시 "
                + startTime.getMinute() + "분";
    }
}

