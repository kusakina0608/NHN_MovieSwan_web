package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ReservationServiceImpl implements ReservationService{
    @Override
    public String register(ReservationDTO dto) {
        log.info("DTO====================");
        log.info(dto);

        Reservation entity = dtoToEntity(dto);

        log.info("Entity====================");
        log.info(entity);

        return null;
    }
}
