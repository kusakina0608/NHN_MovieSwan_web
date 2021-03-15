package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDetailDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.format.DateTimeFormatter;

@ShellComponent
@Log4j2
@RequiredArgsConstructor
public class CliController {
    private final ReservationService service;

    @ShellMethod("예매내역 조회")
    public String reservationList(@ShellOption String memberId) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<ReservationDetailDTO, Reservation> result = service.getMyReservationList(pageRequestDTO, memberId);

        if(result.getDtoList().isEmpty())
            return "예매 내역이 없습니다.";

        StringBuilder message = new StringBuilder();
        message.append("예매 번호\t\t영화 제목\t상영 일자\t\t예매 일자\n");
        result.getDtoList().stream().forEach(reservation -> {
            message.append(reservation.toSimpleString());
        });

        return message.toString();
    }

    @ShellMethod("예매내역 상세조회")
    public String reservationDetail(@ShellOption String memberId, @ShellOption String reservationId) {
        ReservationDetailDTO reservationDTO = service.getReservation(reservationId);

        if(isValidReservation(memberId, reservationDTO)) {
            log.warn("reservation : {}", reservationDTO);
            log.warn("memberId : {}", memberId);
            return "잘못된 예약 번호입니다.";
        }

        return reservationDTO.toDetailedString();
    }

    private boolean isValidReservation(String memberId, ReservationDetailDTO reservationDTO) {
        return reservationDTO == null || !reservationDTO.getMemberId().equals(memberId);
    }
}
