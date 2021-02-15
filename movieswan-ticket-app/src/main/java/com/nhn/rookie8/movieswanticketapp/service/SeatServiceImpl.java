package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import com.nhn.rookie8.movieswanticketapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService{

    private final SeatRepository repository;

    @Override
    public String register(SeatDTO dto) {
        log.info("DTO====================");
        log.info(dto);

        Seat entity = dtoToEntity(dto);

        log.info("Entity====================");
        log.info(entity);

        repository.save(entity);

        return entity.getSid();
    }
}
