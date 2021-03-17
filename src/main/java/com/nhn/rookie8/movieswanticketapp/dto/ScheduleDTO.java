package com.nhn.rookie8.movieswanticketapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

    private List<Detail> detail;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        @JsonFormat(pattern = "HH:mm")
        private LocalTime time;
        private String timeTableId;
    }
}
