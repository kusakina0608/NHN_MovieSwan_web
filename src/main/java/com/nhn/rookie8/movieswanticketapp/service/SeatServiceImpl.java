package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.dto.SeatStateDTO;
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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService{

    private final SeatRepository seatRepository;

    @Override
    public List<List<SeatStateDTO>> getAllSeat(String timetableId, int row, int col) {
        List<List<SeatStateDTO>> seats = new ArrayList<>();
        for(char alpha = 'A'; alpha < 'A' + row; alpha++){
            seats.add(new ArrayList<>());
            for(int num = 1; num <= col; num++) {
                String seatCode = new StringBuilder().append(alpha).append(String.format("%02d", num)).toString();
                boolean exist = getReservedSeatList(timetableId).contains(seatCode);
                seats.get(alpha - 'A').add(new SeatStateDTO().builder().seatCode(seatCode).available(!exist).build());
            }
        }
        return seats;
    }

    @Override
    @Transactional
    public void confirmSeat(String timetableId, String memberId, String reservationId, String seats) {
        for (String seatCode : seats.split(",")) {
            seatRepository.updateReservationId(reservationId, timetableId, seatCode, memberId);
        }
    }

    @Override
    public List<String> getReservedSeatList(String timetableId) {
        List<String> seatIdList = new ArrayList<>();
        seatRepository.findAll(getReservedSeat(timetableId), PageRequest.of(0, 1000)).toList()
                .forEach(el -> seatIdList.add(el.getSeatCode()));
        return seatIdList;
    }

    @Override
    public List<String> getMySeatList(String reservationId) {
        List<String> seatIdList = new ArrayList<>();
        seatRepository.findAll(getMySeat(reservationId), PageRequest.of(0, 1000)).toList()
                .forEach(el -> seatIdList.add(el.getSeatCode()));
        return seatIdList;
    }

    @Override
    @Transactional(rollbackOn = {DataIntegrityViolationException.class, SQLException.class})
    public Boolean preemptSeat(SeatDTO dto) {
        Seat seat = dtoToEntity(dto);
        seatRepository.save(seat);
        return true;
    }

    @Override
    public Boolean cancelSeat(SeatDTO dto) {
        Seat seat = dtoToEntity(dto);
        Optional<Seat> result = seatRepository.findById(
                SeatId.builder().timetableId(dto.getTimetableId()).seatCode(dto.getSeatCode()).build());
        if(result.isPresent() && isSameMember(seat, result.get())) {
            List<Seat> deleteList = new ArrayList<>();
            deleteList.add(seat);
            seatRepository.deleteInBatch(deleteList);
            return true;
        }
        return false;
    }

    @Scheduled(fixedDelay = 300000)
    public void cancelPreemption(){
        List<Seat> list = seatRepository.findAll(getExpiredSeat(), PageRequest.of(0, 1000)).toList();
        list.forEach(e -> log.info("{} 사용자의 좌석 선점 만료. 상영번호: {}, 좌석번호: {}", e.getMemberId(), e.getTimetableId(), e.getSeatCode()));
        seatRepository.deleteInBatch(list);
    }

    private BooleanBuilder getReservedSeat(String timetableId) {
        return new BooleanBuilder()
                .and(QSeat.seat.timetableId.eq(timetableId));
    }

    private BooleanBuilder getMySeat(String reservationId) {
        return new BooleanBuilder()
                .and(QSeat.seat.reservationId.eq(reservationId));
    }

    private BooleanBuilder getExpiredSeat() {
        return new BooleanBuilder()
                .and(QSeat.seat.regDate.lt(LocalDateTime.now().minusMinutes(5)))
                .and(QSeat.seat.reservationId.isNull());
    }

    private boolean isSameMember(Seat seat1, Seat seat2) {
        return seat2.getMemberId().equals(seat1.getMemberId());
    }
}
