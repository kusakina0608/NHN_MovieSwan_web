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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository repository;

    @Override
    public String createReservationId() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String reservationId;
        do {
            List<String> codeList = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                StringBuilder salt = new StringBuilder();
                Random rnd = new Random();
                while (salt.length() < 4) {
                    int index = (int) (rnd.nextFloat() * alphabet.length());
                    salt.append(alphabet.charAt(index));
                }
                codeList.add(salt.toString());
            }
            reservationId = codeList.get(0);
            for(int i = 1; i < 4; i++){
                reservationId += '-';
                reservationId += codeList.get(i);
            }
        } while(checkExist(reservationId));

        return reservationId;
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

    private BooleanBuilder getMyList(PageRequestDTO requestDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReservation qReservation = QReservation.reservation;
        // TODO: get user id from session;
        String userId = "kusakina0608";
        // TODO: 아직 관람하지 않은 영화만 리스트로 가져오도록 구현
        BooleanExpression expression = qReservation.uid.eq(userId);
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}
