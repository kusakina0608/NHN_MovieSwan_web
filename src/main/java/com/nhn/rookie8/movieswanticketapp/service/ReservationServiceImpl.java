package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDetailDTO;
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
    public void delete(String reservationId) {
        repository.deleteById(reservationId);
    }

    @Override
    public PageResultDTO<ReservationDetailDTO, Reservation> getMyReservationList(PageRequestDTO requestDTO, String memberId) {
        Page<Reservation> result = repository.findAll(
                getMemberInfo(memberId),
                requestDTO.getPageable(Sort.by("regDate").descending())
        );

        return new PageResultDTO<>(result, this::entityToDetailDto);
    }

    @Override
    public ReservationDetailDTO getReservation(String reservationId) {
        Optional<Reservation> result = repository.findById(reservationId);
        return result.isPresent() ? entityToDetailDto(result.get()) : null;
    }

    private ReservationDetailDTO entityToDetailDto(Reservation reservation) {
        return ReservationDetailDTO.builder()
                .reservationId(reservation.getReservationId())
                .memberId(reservation.getMemberId()).poster(repository.getMoviePoster(reservation.getTimetableId()))
                .title(repository.getMovieName(reservation.getTimetableId()))
                .youngNum(reservation.getYoungNum())
                .adultNum(reservation.getAdultNum())
                .elderNum(reservation.getElderNum())
                .totalNum(reservation.getTotalNum())
                .price(reservation.getPrice())
                .seats(repository.getSeatList(reservation.getReservationId()))
                .startTime(repository.getStartTime(reservation.getTimetableId()))
                .payDate(reservation.getPayDate())
                .build();
    }

    private BooleanBuilder getMemberInfo(String memberId) {
        return new BooleanBuilder().and(QReservation.reservation.memberId.eq(memberId));
    }
}
