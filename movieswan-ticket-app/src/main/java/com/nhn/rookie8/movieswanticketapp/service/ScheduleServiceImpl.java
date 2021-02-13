package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.ScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Schedule;
import com.nhn.rookie8.movieswanticketapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository repository;

   @Override
   public String registerSchedule(ScheduleDTO dto) {
       Schedule entity = dtoToEntity(dto);
       repository.save(entity);
       return entity.getTid();
   }
}
