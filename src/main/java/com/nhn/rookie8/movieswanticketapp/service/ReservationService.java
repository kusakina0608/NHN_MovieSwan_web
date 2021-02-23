package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;

import java.time.LocalDateTime;

public interface ReservationService {
    String createReservationId();
    String register(ReservationDTO dto);
    String delete(ReservationDTO dto);

    PageResultDTO<ReservationDTO, Reservation> getList(PageRequestDTO requestDTO);
    PageResultDTO<ReservationResultDTO, Reservation> getMypageList(PageRequestDTO requestDTO, String uid);
    ReservationResultDTO readReservation(String rid);

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

    default ReservationResultDTO entityToDto(Reservation entity, String mid, String movieName, String imagePath, LocalDateTime startDate){
        ReservationResultDTO dto = ReservationResultDTO.builder()
                .rid(entity.getRid())
                .tid(entity.getTid())
                .uid(entity.getUid())
                .childNum(entity.getChildNum())
                .adultNum(entity.getAdultNum())
                .oldNum(entity.getOldNum())
                .totalNum(entity.getTotalNum())
                .price(entity.getPrice())
                .regDate(entity.getRegDate())
                .mid(mid)
                .movieName(movieName)
                .imgPath(imagePath)
                .startDate(startDate)
                .build();
        return dto;
    }

}
