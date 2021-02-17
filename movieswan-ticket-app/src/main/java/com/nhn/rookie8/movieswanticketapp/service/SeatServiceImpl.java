package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService{

    private final SeatRepository repository;

    @Override
    public String register(SeatDTO dto) {
        log.info("DTO====================");
        log.info(dto);

        Seat entity = dtoToEntity(dto);

        log.info("Entity====================");
        log.info(entity);

        repository.save(entity);

        return entity.getSid();
    }

    @Override
    public List<String> getReservedSeatList(String tid) {
        BooleanBuilder booleanBuilder = getReservedSeat(tid);
        Pageable pageable = PageRequest.of(0, (int)repository.count());
        List<Seat> seatList = repository.findAll(booleanBuilder, pageable).toList();
        List<String> seatIdList = new ArrayList<String>();
        seatList.forEach(el -> {
            seatIdList.add(el.getSid());
        });
        return seatIdList;
    }

    private BooleanBuilder getReservedSeat(String tid) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QSeat qSeat = QSeat.seat;
        BooleanExpression expression = qSeat.tid.eq(tid);
        return booleanBuilder;
    }

    @Override
    @Transactional(rollbackOn = {DataIntegrityViolationException.class})
    public Boolean preempt(SeatDTO dto) {
        Seat entity = dtoToEntity(dto);

        Optional<Seat> result = repository.findById(new SeatId(dto.getTid(), dto.getSid()));
        if(result.isPresent() && !(result.get().getUid().equals(dto.getUid()))){
            System.out.println("다른 사람이 이미 선점했어");
            return false;
        }
        else if(result.isPresent() && result.get().getUid().equals(dto.getUid())){
            System.out.println("여긴 이미 내 자리야");
            return true;
        }
        else {
            repository.save(entity);
            System.out.println("성공했어");
            return true;
        }
    }

    @Override
    public Boolean remove(SeatDTO dto) {
        SeatId seatId = SeatId.builder().tid(dto.getTid()).sid(dto.getSid()).build();
        Optional<Seat> result = repository.findById(seatId);
        if(result.isPresent() && result.get().getUid().equals(dto.getUid())) {
            List<Seat> deleteList = new ArrayList<Seat>();
            deleteList.add(dtoToEntity(dto));
            repository.deleteInBatch(deleteList);
            return true;
        }
        else{
            return false;
        }
    }
}
