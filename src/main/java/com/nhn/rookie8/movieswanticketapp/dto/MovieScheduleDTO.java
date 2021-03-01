package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieScheduleDTO {
    private String timetableId;
    private String movieId;
    private LocalDateTime startTime;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}

