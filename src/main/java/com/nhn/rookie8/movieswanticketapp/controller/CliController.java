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
        for (ReservationDetailDTO reservationDTO : result.getDtoList()) {
            message.append(reservationDTO.getReservationId()).append("\t")
                    .append(reservationDTO.getTitle()).append("\t")
                    .append(reservationDTO.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\t")
                    .append(reservationDTO.getPayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\t")
                    .append("\n");
        }

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

        StringBuilder message = new StringBuilder();
        message.append("예약 번호 : ").append(reservationId).append("\n")
                .append("영화 제목 : ").append(reservationDTO.getTitle()).append("\n")
                .append("시작 시간 : ").append(reservationDTO.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n")
                .append("예약 일자 : ").append(reservationDTO.getPayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n")
                .append("좌석 정보 : ");
        for(String seat : reservationDTO.getSeats())
            message.append(seat).append(" ");
        message.append("\n").append("결제 정보").append("\n")
                .append("성인 : ").append(reservationDTO.getAdultNum()).append("\t")
                .append("청소년 : ").append(reservationDTO.getYoungNum()).append("\t")
                .append("우대 : ").append(reservationDTO.getElderNum()).append("\n")
                .append("총 결제 금액 : ").append(reservationDTO.getPrice());

        return message.toString();
    }

    private boolean isValidReservation(String memberId, ReservationDetailDTO reservationDTO) {
        return reservationDTO == null || !reservationDTO.getMemberId().equals(memberId);
    }
}
