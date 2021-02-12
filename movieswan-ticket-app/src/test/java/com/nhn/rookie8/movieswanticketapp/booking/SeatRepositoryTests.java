package com.nhn.rookie8.movieswanticketapp.booking;

import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import com.nhn.rookie8.movieswanticketapp.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class SeatRepositoryTests {
    @Autowired
    SeatRepository seatRepository;

    @Test
    public void testClass(){
        System.out.println(seatRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Seat seat = Seat.builder()
                    .rid("SAMPLE-RID-" + String.format("%03d", i))
                    .sid("A" + String.format("%03d", i))
                    .build();
            seatRepository.save(seat);
        });
    }

    @Test
    public void testSelect(){
        SeatId seatId = new SeatId("SAMPLE-RID-077", "A077");

        Optional<Seat> result = seatRepository.findById(seatId);
        System.out.println("=============================================");
        if(result.isPresent()){
            Seat seat = result.get();
            System.out.println(seat);
        }
    }
}
