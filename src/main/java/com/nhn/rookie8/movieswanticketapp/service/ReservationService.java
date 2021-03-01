package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;

public interface ReservationService {
    ReservationDTO reserve(ReservationDTO dto);
    void delete(ReservationDTO dto);

    PageResultDTO<ReservationDTO, Reservation> getMyReservationList(PageRequestDTO requestDTO, String uid);
    ReservationDTO getReservation(String rid);

    String getReservationInfo(ReservationDTO reservation);
    String getReservationDetail(ReservationDTO reservation);

    default Reservation dtoToEntity(ReservationDTO reservationDTO){
        return Reservation.builder()
                .reservationId(reservationDTO.getReservationId())
                .timetableId(reservationDTO.getTimetableId())
                .memberId(reservationDTO.getMemberId())
                .youngNum(reservationDTO.getYoungNum())
                .adultNum(reservationDTO.getAdultNum())
                .elderNum(reservationDTO.getElderNum())
                .totalNum(reservationDTO.getTotalNum())
                .price(reservationDTO.getPrice())
                .build();
    }

    default ReservationDTO entityToDto(Reservation reservation){
        return ReservationDTO.builder()
                .reservationId(reservation.getReservationId())
                .timetableId(reservation.getTimetableId())
                .memberId(reservation.getMemberId())
                .youngNum(reservation.getYoungNum())
                .adultNum(reservation.getAdultNum())
                .elderNum(reservation.getElderNum())
                .totalNum(reservation.getTotalNum())
                .price(reservation.getPrice())
                .payDate(reservation.getRegDate())
                .regDate(reservation.getRegDate())
                .modDate(reservation.getModDate())
                .build();
    }
}
