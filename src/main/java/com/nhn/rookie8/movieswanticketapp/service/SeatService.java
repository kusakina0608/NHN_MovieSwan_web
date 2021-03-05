package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;

import java.util.List;

public interface SeatService {
    List<List<String>> getAllSeat(int row, int col);
    String register(SeatDTO dto);
    void confirmSeat(String timetableId, String memberId, String reservationId, String seatCode);
    List<String> getReservedSeatList(String timetableId);
    List<String> getMySeatList(String reservationId);
    Boolean preempt(SeatDTO dto);
    Boolean remove(SeatDTO dto);

    default Seat dtoToEntity(SeatDTO seatDTO){
        return Seat.builder()
                .timetableId(seatDTO.getTimetableId())
                .seatCode(seatDTO.getSeatCode())
                .memberId(seatDTO.getMemberId())
                .reservationId(seatDTO.getReservationId())
                .build();
    }

    default SeatDTO entityToDto(Seat seat){
        return SeatDTO.builder()
                .timetableId(seat.getTimetableId())
                .seatCode(seat.getSeatCode())
                .memberId(seat.getMemberId())
                .reservationId(seat.getReservationId())
                .regDate(seat.getRegDate())
                .modDate(seat.getModDate())
                .build();
    }
}
