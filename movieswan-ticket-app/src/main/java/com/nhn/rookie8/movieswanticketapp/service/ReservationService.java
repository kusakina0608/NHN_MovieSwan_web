package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;

public interface ReservationService {
    String register(ReservationDTO dto);

    default Reservation dtoToEntity(ReservationDTO dto){
        Reservation entity = Reservation.builder()
                .rid(dto.getRid())
                .tid(dto.getTid())
                .uid(dto.getUid())
                .childNum(dto.getChildNum())
                .adultNum(dto.getAdultNum())
                .oldNum(dto.getOldNum())
                .totalNum(dto.getTotalNum())
                .price(dto.getPrice())
                .build();
        return entity;
    }
}
