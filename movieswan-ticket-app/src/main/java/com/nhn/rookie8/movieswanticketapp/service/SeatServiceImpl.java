package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import com.nhn.rookie8.movieswanticketapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
    @Transactional
    public Boolean preempt(SeatDTO dto) {
        Optional<Seat> result = repository.findById(new SeatId(dto.getTid(), dto.getSid()));
        if(result.isPresent() && !(result.get().getUid().equals("kusakina0608"))){
            System.out.println("선점 불가능");
            return false;
        }
        else{
            System.out.println("선점 가능");
            Seat entity = dtoToEntity(dto);
            if(entity.isNew())
                repository.save(entity);
            return true;
        }
    }
}
