package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class CliController {
    private final ReservationService service;

    @ShellMethod("예매내역 조회")
    public String rsvs(@ShellOption String uid) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<ReservationDTO, Reservation> result = service.getMyReservationList(pageRequestDTO, uid);
        StringBuilder message = new StringBuilder();
        for (ReservationDTO reservationDTO : result.getDtoList())
            message.append(service.getReservationInfo(reservationDTO));

        return message.toString();
    }
}
