package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import com.nhn.rookie8.movieswanticketapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
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
    @Transactional(rollbackOn = {DataIntegrityViolationException.class})
    public Boolean preempt(SeatDTO dto) {
        Seat entity = dtoToEntity(dto);

        Optional<Seat> result = repository.findById(new SeatId(dto.getTid(), dto.getSid()));
        // TODO: 전달받은 ID와 비교하는 코드 작성
        if(result.isPresent() && !(result.get().getUid().equals("kusakina0608"))){
            System.out.println("다른 사람이 이미 선점했어");
            return false;
        }
        // TODO: 전달받은 ID와 비교하는 코드 작성
        else if(result.isPresent() && result.get().getUid().equals("kusakina0608")){
            System.out.println("여긴 이미 내 자리야");
            return true;
        }
        else {
            repository.save(entity);
            System.out.println("성공했어");
            return true;
        }
    }
}
