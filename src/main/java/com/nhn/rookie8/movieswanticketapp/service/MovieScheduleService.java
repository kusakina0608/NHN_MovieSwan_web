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
    String registerMovieSchedule(MovieScheduleInputDTO dto);
    String deleteMovieSchedule(String timetableId);

    MovieScheduleDTO getASchedule(String timetableId);
    List<MovieScheduleDTO> getAllSchedulesOfMovie(String movieId);

    default MovieSchedule dtoToEntity(MovieScheduleInputDTO movieScheduleInputDTO) {
        LocalDate date = LocalDate.parse(movieScheduleInputDTO.getStartDate());
        LocalTime time = LocalTime.parse(movieScheduleInputDTO.getStartTime());
        LocalDateTime datetime = LocalDateTime.of(date, time);

        StringBuilder builder = new StringBuilder();
        builder.append("aaa").append(datetime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));

        String timetableId = builder.toString();

        return MovieSchedule.builder()
                .timetableId(timetableId)
                .movieId(movieScheduleInputDTO.getMovieId())
                .startTime(datetime)
                .build();
    }

    default MovieScheduleDTO entityToDto(MovieSchedule entity) {
        return MovieScheduleDTO.builder()
                .timetableId(entity.getTimetableId())
                .movieId(entity.getMovieId())
                .startTime(entity.getStartTime())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }
}
