package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface MovieScheduleService {
    void registerMovieSchedule(MovieScheduleInputDTO dto);
    void deleteMovieSchedule(String tid);

    MovieScheduleDTO getASchedule(String tid);
    List<MovieScheduleDTO> getAllSchedulesOfMovie(String mid);

    default MovieSchedule dtoToEntity(MovieScheduleInputDTO movieScheduleInputDTO) {
        LocalDate date = LocalDate.parse(movieScheduleInputDTO.getDate());
        LocalTime time = LocalTime.parse(movieScheduleInputDTO.getTime());
        LocalDateTime datetime = LocalDateTime.of(date, time);

        StringBuilder builder = new StringBuilder();
        builder.append("aaa").append(datetime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));

        String tid = builder.toString();

        MovieSchedule entity = MovieSchedule.builder()
                .tid(tid)
                .mid(movieScheduleInputDTO.getMid())
                .time(datetime)
                .build();
        return entity;
    }

    default MovieScheduleDTO entityToDTO(MovieSchedule entity) {
        MovieScheduleDTO movieScheduleDTO = MovieScheduleDTO.builder()
                .tid(entity.getTid())
                .mid(entity.getMid())
                .time(entity.getTime())
                .build();
        return movieScheduleDTO;
    }
}
