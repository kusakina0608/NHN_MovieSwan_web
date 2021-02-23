package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;

public interface ReservationService {
    String createReservationId();
    String register(ReservationDTO dto);
    String delete(ReservationDTO dto);

    PageResultDTO<ReservationDTO, Reservation> getMyReservationList(PageRequestDTO requestDTO, String uid);
    ReservationDTO getReservation(String rid);

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

    default ReservationDTO entityToDto(Reservation entity){
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .rid(entity.getRid())
                .tid(entity.getTid())
                .uid(entity.getUid())
                .childNum(entity.getChildNum())
                .adultNum(entity.getAdultNum())
                .oldNum(entity.getOldNum())
                .totalNum(entity.getTotalNum())
                .price(entity.getPrice())
                .regDate(entity.getRegDate())
                .build();
        return reservationDTO;
    }
}
