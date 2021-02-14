package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;

public interface SeatService {
    SeatId register(SeatDTO dto);

    default Seat dtoToEntity(SeatDTO dto){
        Seat entity = Seat.builder()
                .rid(dto.getRid())
                .sid(dto.getSid())
                .build();
        return entity;
    }
}
