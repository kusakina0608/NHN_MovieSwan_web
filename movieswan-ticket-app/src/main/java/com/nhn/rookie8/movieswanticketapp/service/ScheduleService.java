package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.ScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Schedule;

import java.time.LocalDateTime;

public interface ScheduleService {
    String registerSchedule(ScheduleDTO dto);

    default Schedule dtoToEntity(ScheduleDTO dto) {
        Schedule entity = Schedule.builder()
                .tid(dto.getTid())
                .mid(dto.getMid())
                .time(dto.getTime())
                .build();
        return entity;
    }
}
