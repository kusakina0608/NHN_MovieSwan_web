package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;

import java.util.List;

public interface SeatService {
    String register(SeatDTO dto);
    void modify(List<SeatDTO> dto, String rid);
    List<String> getReservedSeatList(String tid);
    List<String> getMySeatList(String rid);
    Boolean preempt(SeatDTO dto);
    Boolean remove(SeatDTO dto);

    default Seat dtoToEntity(SeatDTO dto){
        return Seat.builder()
                .tid(dto.getTid())
                .sid(dto.getSid())
                .uid(dto.getUid())
                .rid(dto.getRid())
                .build();
    }

    default SeatDTO entityToDto(Seat entity){
        return SeatDTO.builder()
                .tid(entity.getTid())
                .sid(entity.getSid())
                .uid(entity.getSid())
                .rid(entity.getRid())
                .regDate(entity.getRegDate())
                .build();
    }
}
