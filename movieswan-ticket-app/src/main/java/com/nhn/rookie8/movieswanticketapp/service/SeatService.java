package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;

public interface SeatService {
    String register(SeatDTO dto);

    Boolean preempt(SeatDTO dto) throws InterruptedException;

    default Seat dtoToEntity(SeatDTO dto){
        Seat entity = Seat.builder()
                .tid(dto.getTid())
                .sid(dto.getSid())
                .uid(dto.getUid())
                .rid(dto.getRid())
                .build();
        return entity;
    }

    default SeatDTO entityToDto(Seat entity){
        SeatDTO dto = SeatDTO.builder()
                .tid(entity.getTid())
                .sid(entity.getSid())
                .uid(entity.getSid())
                .rid(entity.getRid())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }
}
