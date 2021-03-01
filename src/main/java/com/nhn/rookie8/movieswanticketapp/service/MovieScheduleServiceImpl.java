package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import com.nhn.rookie8.movieswanticketapp.repository.MovieScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final MovieScheduleRepository repository;

    @Override
    public String registerMovieSchedule(MovieScheduleInputDTO movieScheduleInputDTO) {
        try {
            MovieSchedule entity = dtoToEntity(movieScheduleInputDTO);
            repository.save(entity);

            log.info("Registered Entity : {}", entity);

            return entity.getTimetableId();
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public String deleteMovieSchedule(String timeTableId) {
        try {
            repository.deleteById(timeTableId);

            log.info("Deleted TimetableID : {}", timeTableId);

            return timeTableId;
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public MovieScheduleDTO getASchedule(String timeTableId) {
        try {
            Optional<MovieSchedule> result = repository.findById(timeTableId);

            log.info("Search Result : {}", result.isPresent() ? result : "No Result");

            return result.isPresent() ? entityToDto(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<MovieScheduleDTO> getAllSchedulesOfMovie(String movieId) {
        try {
            List<MovieSchedule> result = repository.findByMovieIdOrderByStartTimeAsc(movieId);
            List<MovieScheduleDTO> schedulesList = new ArrayList<>();

            log.info("Search Results : {}", result);

            result.forEach(e -> schedulesList.add(entityToDto(e)));
            return schedulesList;
        } catch (Exception e) {
            log.error(e);
            return Collections.emptyList();
        }
    }
}