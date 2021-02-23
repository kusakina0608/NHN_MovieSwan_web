package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInputDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.repository.MovieScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final MovieScheduleRepository repository;

    @Override
    public void registerMovieSchedule(MovieScheduleInputDTO movieScheduleInputDTO) {
        MovieSchedule entity = dtoToEntity(movieScheduleInputDTO);
        repository.save(entity);
    }

    @Override
    public void deleteMovieSchedule(String timeTableId) {
        repository.deleteById(timeTableId);
    }

    @Override
    public MovieScheduleDTO getASchedule(String timeTableId) {
        Optional<MovieSchedule> result = repository.findById(timeTableId);
        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public List<MovieScheduleDTO> getAllSchedulesOfMovie(String mid) {
        List<MovieSchedule> result = repository.findByMidOrderByTimeAsc(mid);
        List<MovieScheduleDTO> schedulesList = new ArrayList<> ();

        result.forEach(e -> schedulesList.add(entityToDTO(e)));
        return schedulesList;
    }
}