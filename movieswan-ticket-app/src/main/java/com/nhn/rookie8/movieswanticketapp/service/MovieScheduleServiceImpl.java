package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import com.nhn.rookie8.movieswanticketapp.repository.MovieScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final MovieScheduleRepository repository;

   @Override
   public String registerMovieSchedule(MovieScheduleInpDTO dto) {
       MovieSchedule entity = dtoToEntity(dto);
       repository.save(entity);
       return entity.getTid();
   }
}
