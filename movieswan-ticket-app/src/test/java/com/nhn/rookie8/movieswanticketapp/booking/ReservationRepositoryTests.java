package com.nhn.rookie8.movieswanticketapp.booking;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import com.nhn.rookie8.movieswanticketapp.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class ReservationRepositoryTests {

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    public void testClass(){
        System.out.println(reservationRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .tid("SAMPLE-TID-" + String.format("%03d", i))
                    .uid("SAMPLE-UID-" + String.format("%03d", i))
                    .rid("SAMPLE-RID-" + String.format("%03d", i))
                    .totalNum(2)
                    .price(26000)
                    .adultNum(2)
                    .childNum(0)
                    .oldNum(0)
                    .build();
            reservationRepository.save(reservation);
        });
    }

    @Test
    public void testSelect(){
        String rid = "SAMPLE-RID-077";

        Optional<Reservation> result = reservationRepository.findById(rid);
        System.out.println("=============================================");
        if(result.isPresent()){
            Reservation reservation = result.get();
            System.out.println(reservation);
        }
    }
}
