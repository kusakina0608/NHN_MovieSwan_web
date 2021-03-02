package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QSeat;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import com.nhn.rookie8.movieswanticketapp.repository.SeatRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService{

    private final SeatRepository repository;

    @Override
    public String register(SeatDTO dto) {
        log.debug("DTO: {}", dto);

        Seat entity = dtoToEntity(dto);

        log.debug("Entity: {}", entity);

        repository.save(entity);

        return entity.getSeatCode();
    }

    @Override
    @Transactional
    public void confirmSeat(String timetableId, String memberId, String reservationId, String seats) {
        String[] seatCodes = seats.split(",");
        for (String seatCode : seatCodes) {
            repository.updateRid(reservationId, timetableId, seatCode, memberId);
        }

    }

    @Override
    public List<String> getReservedSeatList(String tid) {
        BooleanBuilder booleanBuilder = getReservedSeat(tid);
        Pageable pageable = PageRequest.of(0, 1000);
        List<Seat> seatList = repository.findAll(booleanBuilder, pageable).toList();
        List<String> seatIdList = new ArrayList<>();
        seatList.forEach(el -> {
            log.debug("seat: {}", el);
            seatIdList.add(el.getSeatCode());
        });
        return seatIdList;
    }

    @Override
    public List<String> getMySeatList(String rid) {
        BooleanBuilder booleanBuilder = getMySeat(rid);
        Pageable pageable = PageRequest.of(0, 1000);
        List<Seat> seatList = repository.findAll(booleanBuilder, pageable).toList();
        List<String> seatIdList = new ArrayList<>();
        seatList.forEach(el -> seatIdList.add(el.getSeatCode()));
        return seatIdList;
    }

    private BooleanBuilder getReservedSeat(String tid) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QSeat qSeat = QSeat.seat;
        BooleanExpression expression = qSeat.timetableId.eq(tid);
        booleanBuilder.and(expression);
        return booleanBuilder;
    }

    private BooleanBuilder getMySeat(String rid) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QSeat qSeat = QSeat.seat;
        BooleanExpression expression = qSeat.reservationId.eq(rid);
        booleanBuilder.and(expression);
        return booleanBuilder;
    }

    @Override
    @Transactional(rollbackOn = {DataIntegrityViolationException.class})
    public Boolean preempt(SeatDTO dto) {
        SeatId seatId = SeatId.builder().timetableId(dto.getTimetableId()).seatCode(dto.getSeatCode()).build();
        Optional<Seat> result = repository.findById(seatId);
        if(result.isPresent() && !(result.get().getMemberId().equals(dto.getMemberId()))){
            return false;
        }
        repository.save(dtoToEntity(dto));
        return true;
    }

    @Override
    public Boolean remove(SeatDTO dto) {
        SeatId seatId = SeatId.builder().timetableId(dto.getTimetableId()).seatCode(dto.getSeatCode()).build();
        Optional<Seat> result = repository.findById(seatId);
        if(result.isPresent() && result.get().getMemberId().equals(dto.getMemberId())) {
            List<Seat> deleteList = new ArrayList<>();
            deleteList.add(dtoToEntity(dto));
            repository.deleteInBatch(deleteList);
            return true;
        }
        else{
            return false;
        }
    }

    @Scheduled(fixedDelay = 300000)
    public void cancelPreemption(){
        BooleanBuilder booleanBuilder = getExpiredSeat();
        Pageable pageable = PageRequest.of(0, 1000);
        List<Seat> list = repository.findAll(booleanBuilder, pageable).toList();
        list.forEach(e -> log.info("{} 사용자의 좌석 선점 만료. 상영번호: {}, 좌석번호: {}", e.getMemberId(), e.getTimetableId(), e.getSeatCode()));
        repository.deleteInBatch(list);
    }

    private BooleanBuilder getExpiredSeat() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QSeat qSeat = QSeat.seat;
        BooleanExpression expression1 = qSeat.regDate.lt(LocalDateTime.now().minusMinutes(5));
        BooleanExpression expression2 = qSeat.reservationId.isNull();
        booleanBuilder.and(expression1);
        booleanBuilder.and(expression2);
        return booleanBuilder;
    }
}
