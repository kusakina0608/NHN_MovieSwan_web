package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface MovieScheduleService {
    String registerSchedule(MovieScheduleInpDTO dto);

    default MovieSchedule dtoToEntity(MovieScheduleInpDTO dto) {
        String datetime = dto.getDate() + ' ' + dto.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(datetime, formatter);

        MovieSchedule entity = MovieSchedule.builder()
                .mid(dto.getMid())
                .time(time)
                .build();
        return entity;
    }
}
