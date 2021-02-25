package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QReservation;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.repository.ReservationRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository repository;
    private final Random random = new SecureRandom();

    @Override
    public String createReservationId() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder reservationId = new StringBuilder();
        do {
            for(int i = 0; i < 4; i++){
                StringBuilder salt = new StringBuilder();
                while (salt.length() < 4) {
                    int index = (int) (random.nextFloat() * alphabet.length());
                    salt.append(alphabet.charAt(index));
                }
                reservationId.append(salt.toString());
                if(i < 3) reservationId.append("-");
            }
        } while(checkExist(reservationId.toString()));
        return reservationId.toString();
    }

    private boolean checkExist(String rid) {
        return repository.findById(rid).isPresent();
    }

    @Override
    public String register(ReservationDTO dto) {
        log.info("DTO====================");
        log.info(dto);

        Reservation entity = dtoToEntity(dto);

        log.info("Entity====================");
        log.info(entity);

        repository.save(entity);

        return entity.getRid();
    }

    @Override
    public void delete(ReservationDTO dto) {
        String rid = dto.getRid();
        repository.deleteById(rid);
    }

    @Override
    public PageResultDTO<ReservationDTO, Reservation> getMyReservationList(PageRequestDTO requestDTO, String uid) {
        try {
            Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
            BooleanBuilder booleanBuilder = getUserInfo(uid);
            Page<Reservation> result = repository.findAll(booleanBuilder, pageable);
            Function<Reservation, ReservationDTO> fn = (this::entityToDto);
            return new PageResultDTO<>(result, fn);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public ReservationDTO getReservation(String rid) {
        try {
            Optional<Reservation> result = repository.findById(rid);
            return result.isPresent() ? entityToDto(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public String getReservationInfo(ReservationDTO reservation) {
        StringBuilder info = new StringBuilder();
        String title = repository.getMovieName(reservation.getTid());
        LocalDateTime startTime = repository.getStartTime(reservation.getTid());

        info.append(reservation.getRid() + "\t");
        info.append(title + "\t");
        info.append(startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\t");
        info.append(reservation.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

        return info.toString();
    }

    @Override
    public String getReservationDetail(ReservationDTO reservation) {
        StringBuilder detail = new StringBuilder();
        String title = repository.getMovieName(reservation.getTid());
        LocalDateTime startTime = repository.getStartTime(reservation.getTid());
        List<String> seats = repository.getSeatList(reservation.getRid());

        detail.append("예약 번호 : " + reservation.getRid() + "\n");
        detail.append("영화 제목 : " + title + "\n");
        detail.append("상영 일자 : " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n");
        detail.append("예약 일자 : " + reservation.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
        detail.append("좌석 정보 : ");
        for(String seat : seats)
            detail.append(seat + " ");
        detail.append("\n결제 정보\n");
        detail.append("성인 : " + reservation.getAdultNum() + "\t");
        detail.append("청소 : " + reservation.getChildNum() + "\t");
        detail.append("우대 : " + reservation.getOldNum() + "\n");
        detail.append("총 결제 금액 : " + reservation.getPrice());

        return detail.toString();
    }

    private BooleanBuilder getUserInfo(String uid) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReservation qReservation = QReservation.reservation;

        BooleanExpression expression = qReservation.uid.eq(uid);
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}
