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
@Log4j2
@RequiredArgsConstructor
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final MovieScheduleRepository repository;

    @Override
    public String registerMovieSchedule(MovieScheduleInputDTO movieScheduleInputDTO) {
        try {
            MovieSchedule entity = dtoToEntity(movieScheduleInputDTO);
            log.info("Entity : {}", entity);
            repository.save(entity);
            return entity.getTid();
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public String deleteMovieSchedule(String timeTableId) {
        try {
            log.info("Deleted tid : {}", timeTableId);
            repository.deleteById(timeTableId);
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
            return result.isPresent() ? entityToDTO(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<MovieScheduleDTO> getAllSchedulesOfMovie(String mid) {
        try {
            List<MovieSchedule> result = repository.findByMidOrderByTimeAsc(mid);
            List<MovieScheduleDTO> schedulesList = new ArrayList<>();

            result.forEach(e -> schedulesList.add(entityToDTO(e)));
            return schedulesList;
        } catch (Exception e) {
            log.error(e);
            return Collections.EMPTY_LIST;
        }
    }
}