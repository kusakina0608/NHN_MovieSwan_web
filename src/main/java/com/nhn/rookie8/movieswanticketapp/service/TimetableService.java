package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Timetable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface TimetableService {
    String registerTimetable(TimetableInputDTO dto);
    String deleteTimetable(String tid);

    TimetableDTO getTimetable(String tid);
    List<TimetableDTO> getAllTimetable(String mid);

    default Timetable dtoToEntity(TimetableInputDTO timetableInputDTO) {
        LocalDate date = LocalDate.parse(timetableInputDTO.getDate());
        LocalTime time = LocalTime.parse(timetableInputDTO.getTime());
        LocalDateTime datetime = LocalDateTime.of(date, time);

        StringBuilder builder = new StringBuilder();
        builder.append("AAA").append(datetime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));

        String timetableId = builder.toString();

        return Timetable.builder()
                .timetableId(timetableId)
                .movieId(timetableInputDTO.getMovieId())
                .startTime(datetime)
                .build();
    }

    default TimetableDTO entityToDTO(Timetable timetable) {
        return TimetableDTO.builder()
                .timetableId(timetable.getTimetableId())
                .movieId(timetable.getMovieId())
                .startTime(timetable.getStartTime())
                .regDate(timetable.getRegDate())
                .modDate(timetable.getModDate())
                .build();
    }
}
