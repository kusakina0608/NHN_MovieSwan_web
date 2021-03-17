package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Log4j2
public class WebHookImpl implements WebHook {
    private final RestTemplate restTemplate;

    @Override
    public void sendReservationSuccessMessage(MemberDTO memberDTO, MovieDTO movieDTO, ReservationDTO reservationDTO){
        ReservationAlarmDTO reservationAlarmDTO = ReservationAlarmDTO.builder()
                .botName("Movie Swan")
                .botIconImage("https://daejin-lee.github.io/assets/profile.png")
                .text("예매가 완료되었습니다.")
                .attachments(new ArrayList<>())
                .build();
        StringBuilder titleMsg = new StringBuilder();
        titleMsg.append(memberDTO.getName());
        titleMsg.append("님 ");
        titleMsg.append(movieDTO.getTitle());
        titleMsg.append(" 예매가 완료되었습니다.");
        StringBuilder textMsg = new StringBuilder();
        textMsg.append(memberDTO.getName());
        textMsg.append("님 안녕하세요.");
        textMsg.append(movieDTO.getTitle());
        textMsg.append(" 예매가 완료되었습니다.\n예매번호: ");
        textMsg.append(reservationDTO.getReservationId());
        reservationAlarmDTO.getAttachments().add(
                AttachmentDTO.builder()
                        .title(titleMsg.toString())
                        .titleLink("dev-movieswan.nhn.com/")
                        .text(textMsg.toString())
                        .color("blue")
                        .build()
        );
        log.info("memberDTO.getUrl(): {}", memberDTO.getUrl());
        restTemplate.postForObject(memberDTO.getUrl(), reservationAlarmDTO, ReservationAlarmDTO.class);
    }

    @Override
    public void sendReservationCanceledMessage(MemberDTO memberDTO, ReservationDetailDTO reservationDetailDTO) {
        StringBuilder titleMsg = new StringBuilder();
        StringBuilder textMsg = new StringBuilder();

        ReservationAlarmDTO reservationAlarmDTO = ReservationAlarmDTO.builder()
                .botName("Movie Swan")
                .botIconImage("https://daejin-lee.github.io/assets/profile.png")
                .text("예매가 취소되었습니다.")
                .attachments(new ArrayList<>())
                .build();
        titleMsg.append(memberDTO.getName())
                .append("님, ")
                .append(reservationDetailDTO.getTitle())
                .append(" 의 예매가 취소되었습니다.");
        textMsg.append(memberDTO.getName())
                .append("님 안녕하세요, 영화 ")
                .append(reservationDetailDTO.getTitle())
                .append(" 의 예매 취소가 완료되었습니다\n예매 번호 : ")
                .append(reservationDetailDTO.getReservationId())
                .append("\n취소 시간 : ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        reservationAlarmDTO.getAttachments().add(
                AttachmentDTO.builder()
                        .title(titleMsg.toString())
                        .titleLink("dev-movieswan.nhn.com/")
                        .text(textMsg.toString())
                        .color("red")
                        .build()
        );

        log.info("memberDTO.getUrl(): {}", memberDTO.getUrl());
        restTemplate.postForObject(memberDTO.getUrl(), reservationAlarmDTO, ReservationAlarmDTO.class);
    }
}
