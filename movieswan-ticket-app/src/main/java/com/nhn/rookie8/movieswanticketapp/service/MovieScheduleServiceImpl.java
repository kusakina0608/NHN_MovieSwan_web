package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import com.nhn.rookie8.movieswanticketapp.repository.MovieScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final MovieScheduleRepository repository;

    @Override
    public void registerMovieSchedule(MovieScheduleInpDTO dto) {
        MovieSchedule entity = dtoToEntity(dto);
        repository.save(entity);
    }

    @Override
    public void deleteMovieSchedule(String tid) {
        repository.deleteById(tid);
    }

    @Override
    public List<MovieScheduleDTO> getMovieSchedule(String mid) {
        List<MovieSchedule> result = repository.findByMidOrderByTimeAsc(mid);
        List<MovieScheduleDTO> scheduleList = new ArrayList<MovieScheduleDTO> ();

        result.forEach(e -> {
            scheduleList.add(entityToDTO(e));
        });
        return scheduleList;
    }
}