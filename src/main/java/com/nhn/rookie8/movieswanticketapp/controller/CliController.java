package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@Log4j2
@RequiredArgsConstructor
public class CliController {
    private final ReservationService service;

    @ShellMethod("예매내역 조회")
    public String rsvs(@ShellOption String uid) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<ReservationDTO, Reservation> result = service.getMyReservationList(pageRequestDTO, uid);
        StringBuilder message = new StringBuilder();

        if(result.getDtoList().isEmpty())
            return "예매 내역이 없습니다.";

        message.append("예매 번호\t\t영화 제목\t\t\t상영 일자\t예매 일자\n");
        for (ReservationDTO reservationDTO : result.getDtoList())
            message.append(service.getReservationInfo(reservationDTO));

        return message.toString();
    }

    @ShellMethod("예매내역 상세조회")
    public String dtl(@ShellOption String uid, @ShellOption String rid) {
        ReservationDTO reservationDTO = service.getReservation(rid);

        if(reservationDTO == null || !reservationDTO.getMemberId().equals(uid)) {
            log.warn("reservation : {}", reservationDTO);
            log.warn("uid : {}", uid);
            return "잘못된 예약 번호입니다.";
        }

        return service.getReservationDetail(reservationDTO);
    }
}
