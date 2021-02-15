package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface MovieScheduleService {
    void registerMovieSchedule(MovieScheduleInpDTO dto);
    void deleteMovieSchedule(String tid);
    List<MovieScheduleDTO> getMovieSchedule(String mid);

    default MovieSchedule dtoToEntity(MovieScheduleInpDTO dto) {
        String datetime = dto.getDate() + ' ' + dto.getTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(datetime, formatter);

        String tid = "aaa";
        String[] tid_date = {Integer.toString(time.getYear()).substring(2),
                Integer.toString(time.getMonthValue()),
                Integer.toString(time.getDayOfMonth()),
                Integer.toString(time.getHour()),
                Integer.toString(time.getMinute())};

        for (int i = 0; i < tid_date.length; i++) {
            tid_date[i] = tid_date[i].length() < 2 ? '0' + tid_date[i] : tid_date[i];
            tid += tid_date[i];
        }

        System.out.println(tid);

        MovieSchedule entity = MovieSchedule.builder()
                .tid(tid)
                .mid(dto.getMid())
                .time(time)
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
