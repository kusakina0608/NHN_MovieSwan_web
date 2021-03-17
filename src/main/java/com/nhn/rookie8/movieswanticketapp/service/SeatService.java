package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.dto.SeatStateDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;

import java.util.List;

public interface SeatService {
    public List<List<SeatStateDTO>> getAllSeat(String timetableId, String memberId, int row, int col);
    void confirmSeat(String timetableId, String memberId, String reservationId, String seatCode);
    List<String> getSeatListByTimetableId(String timetableId);
    List<String> getSeatListByReservationId(String reservationId);
    void preemptSeat(SeatDTO dto);
    Boolean cancelSeat(SeatDTO dto);

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
