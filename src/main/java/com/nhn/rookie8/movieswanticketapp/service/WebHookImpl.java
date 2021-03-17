package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${baseURL}")
    private String baseUrl;

    @Override
    public void sendReservationSuccessMessage(MemberDTO memberDTO, MovieDTO movieDTO, ReservationDTO reservationDTO){
        ReservationAlarmDTO reservationAlarmDTO = ReservationAlarmDTO.builder()
                .botName(this.botName)
                .botIconImage(this.botIconImage)
                .text("예매가 완료되었습니다.")
                .attachments(new ArrayList<>())
                .build();

        String titleMsg = memberDTO.getName() + "님, " + movieDTO.getTitle() + " 의 예매가 완료되었습니다.";

        String textMsg = memberDTO.getName() + "님 안녕하세요, 영화 " + movieDTO.getTitle()
                + " 의 예매가 완료되었습니다\n예매 번호 : " + reservationDTO.getReservationId();

        reservationAlarmDTO.getAttachments().add(
                AttachmentDTO.builder()
                        .title(titleMsg)
                        .titleLink(baseUrl)
                        .text(textMsg)
                        .color("blue")
                        .build()
        );

        restTemplate.postForObject(memberDTO.getUrl(), reservationAlarmDTO, ReservationAlarmDTO.class);
    }

    @Override
    public void sendReservationCanceledMessage(MemberDTO memberDTO, ReservationDetailDTO reservationDetailDTO) {
        ReservationAlarmDTO reservationAlarmDTO = ReservationAlarmDTO.builder()
                .botName(this.botName)
                .botIconImage(this.botIconImage)
                .text("예매가 취소되었습니다.")
                .attachments(new ArrayList<>())
                .build();

        String titleMsg = memberDTO.getName() + "님, " + reservationDetailDTO.getTitle() + " 의 예매가 취소되었습니다.";

        String textMsg = memberDTO.getName() + "님 안녕하세요, 영화 " + reservationDetailDTO.getTitle()
                + " 의 예매 취소가 완료되었습니다\n예매 번호 : " + reservationDetailDTO.getReservationId()
                + "\n취소 시간 : " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        reservationAlarmDTO.getAttachments().add(
                AttachmentDTO.builder()
                        .title(titleMsg)
                        .titleLink(baseUrl)
                        .text(textMsg)
                        .color("red")
                        .build()
        );

        restTemplate.postForObject(memberDTO.getUrl(), reservationAlarmDTO, ReservationAlarmDTO.class);
    }
}
