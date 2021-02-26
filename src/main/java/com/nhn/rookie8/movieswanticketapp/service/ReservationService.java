package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;

public interface ReservationService {
    String createReservationId();
    String register(ReservationDTO dto);
    void delete(ReservationDTO dto);

    PageResultDTO<ReservationDTO, Reservation> getMyReservationList(PageRequestDTO requestDTO, String uid);
    ReservationDTO getReservation(String rid);

    String getReservationInfo(ReservationDTO reservation);
    String getReservationDetail(ReservationDTO reservation);

    default Reservation dtoToEntity(ReservationDTO dto){
        return Reservation.builder()
                .rid(dto.getRid())
                .tid(dto.getTid())
                .uid(dto.getUid())
                .childNum(dto.getChildNum())
                .adultNum(dto.getAdultNum())
                .oldNum(dto.getOldNum())
                .totalNum(dto.getTotalNum())
                .price(dto.getPrice())
                .build();
    }

    default ReservationDTO entityToDto(Reservation entity){
        return ReservationDTO.builder()
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
    }
}
