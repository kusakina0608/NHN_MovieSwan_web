package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationResultDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository repository;

    @Override
    public String createReservationId() {
        Random rnd = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder reservationId = new StringBuilder();
        do {
            List<String> codeList = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                StringBuilder salt = new StringBuilder();
                while (salt.length() < 4) {
                    int index = (int) (rnd.nextFloat() * alphabet.length());
                    salt.append(alphabet.charAt(index));
                }
                reservationId.append(salt.toString());
                reservationId.append("-");
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
    public String delete(ReservationDTO dto) {
        String rid = dto.getRid();
        repository.deleteById(rid);
        return null;
    }

    @Override
    public PageResultDTO<ReservationDTO, Reservation> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());
        BooleanBuilder booleanBuilder = getMyList(requestDTO);
        Page<Reservation> result = repository.findAll(booleanBuilder, pageable);
        Function<Reservation, ReservationDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public ReservationResultDTO readReservation(String rid) {
        Optional<Reservation> result = repository.findById(rid);

        if (!result.isPresent()) { return null; }

        String tid = result.get().getTid();
        String mid = repository.getMovieMid(tid);
        String name = repository.getMovieName(tid);
        String poster = repository.getMoviePoster(tid);
        LocalDateTime startDate = LocalDateTime.of(
                2000 + Integer.parseInt(tid.substring(3, 5)),
                Integer.parseInt(tid.substring(5, 7)),
                Integer.parseInt(tid.substring(7, 9)),
                Integer.parseInt(tid.substring(9, 11)),
                Integer.parseInt(tid.substring(11, 13)));

        return entityToDto(result.get(), mid, name, poster, startDate);
    }

    @Override
    public PageResultDTO<ReservationResultDTO, Reservation> getMypageList(PageRequestDTO requestDTO, String uid) {
        Pageable pageable = requestDTO.getPageable(Sort.by("regDate").descending());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReservation qReservation = QReservation.reservation;

        BooleanExpression expression = qReservation.uid.eq(uid);
        booleanBuilder.and(expression);

        Page<Reservation> result = repository.findAll(booleanBuilder, pageable);

        Function<Reservation, ReservationResultDTO> fn = (entity -> {
            String mid = repository.getMovieMid(entity.getTid());
            String name = repository.getMovieName(entity.getTid());

            String tid = entity.getTid();
            LocalDateTime startDate = LocalDateTime.of(
                    2000 + Integer.parseInt(tid.substring(3, 5)),
                    Integer.parseInt(tid.substring(5, 7)),
                    Integer.parseInt(tid.substring(7, 9)),
                    Integer.parseInt(tid.substring(9, 11)),
                    Integer.parseInt(tid.substring(11, 13)));

            return entityToDto(entity, mid, name, null, startDate);
        });
        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder getMyReservationList(String uid) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReservation qReservation = QReservation.reservation;
        BooleanExpression expression = qReservation.uid.eq(uid);
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}
