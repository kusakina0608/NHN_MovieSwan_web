package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QReservation;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.repository.ReservationRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository repository;
    private final Random random = new SecureRandom();

    @Override
    public ReservationDTO reserve(ReservationDTO dto) {
        dto.setReservationId(createReservationId());
        return entityToDto(repository.save(dtoToEntity(dto)));
    }

    public String createReservationId() {
        while(true){
            String reservationId = RandomStringUtils.randomAlphanumeric(16).toUpperCase(Locale.ROOT)
                    .replaceFirst("^(.{4})(.{4})(.{4})(.{4})$", "$1-$2-$3-$4");
            if(!checkExist(reservationId))
                return reservationId;
        }
    }

    private boolean checkExist(String reservationId) {
        return repository.findById(reservationId).isPresent();
    }

    @Override
    public void delete(ReservationDTO dto) {
        repository.deleteById(dto.getReservationId());
    }

    @Override
    public PageResultDTO<ReservationDTO, Reservation> getMyReservationList(PageRequestDTO requestDTO, String memberId) {
        try {
            Page<Reservation> result = repository.findAll(
                    getMemberInfo(memberId),
                    requestDTO.getPageable(Sort.by("regDate").descending())
            );
            return new PageResultDTO<>(result, this::entityToDto);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public ReservationDTO getReservation(String reservationId) {
        try {
            Optional<Reservation> result = repository.findById(reservationId);
            return result.isPresent() ? entityToDto(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public String getReservationInfo(ReservationDTO reservation) {
        StringBuilder info = new StringBuilder();
        String title = repository.getMovieName(reservation.getTimetableId());
        LocalDateTime startTime = repository.getStartTime(reservation.getTimetableId());

        info.append(reservation.getReservationId() + "\t");
        info.append(title + "\t");
        info.append(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\t");
        info.append(reservation.getPayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

        return info.toString();
    }

    @Override
    public String getReservationDetail(ReservationDTO reservation) {
        StringBuilder detail = new StringBuilder();
        String title = repository.getMovieName(reservation.getTimetableId());
        LocalDateTime startTime = repository.getStartTime(reservation.getTimetableId());
        List<String> seats = repository.getSeatList(reservation.getReservationId());

        detail.append("예약 번호 : " + reservation.getReservationId() + "\n");
        detail.append("영화 제목 : " + title + "\n");
        detail.append("상영 일자 : " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n");
        detail.append("예약 일자 : " + reservation.getPayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
        detail.append("좌석 정보 : ");
        for(String seat : seats)
            detail.append(seat + " ");
        detail.append("\n결제 정보\n");
        detail.append("성인 : " + reservation.getAdultNum() + "\t");
        detail.append("청소 : " + reservation.getYoungNum() + "\t");
        detail.append("우대 : " + reservation.getElderNum() + "\n");
        detail.append("총 결제 금액 : " + reservation.getPrice());

        return detail.toString();
    }

    private BooleanBuilder getMemberInfo(String memberId) {
        return new BooleanBuilder().and(QReservation.reservation.memberId.eq(memberId));
    }
}
