package com.nhn.rookie8.movieswanticketapp.booking.repository;

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
        IntStream.rangeClosed(1, 28).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .tid("AAA2102" + String.format("%02d", i) + "1800")
                    .uid("hyerin9177")
                    .rid("SEBM-B000-B" + String.format("%03d", i))
                    .totalNum(1)
                    .price(7000)
                    .adultNum(0)
                    .childNum(1)
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

    @Test
    public void testUpdate(){
        String rid = "SAMPLE-RID-100";

        Optional<Reservation> result = reservationRepository.findById(rid);
        if(result.isPresent()){
            Reservation reservation = result.get();
            Reservation newReservation = Reservation.builder()
                    .uid(reservation.getUid())
                    .tid(reservation.getTid())
                    .rid(rid)
                    .adultNum(1)
                    .childNum(2)
                    .oldNum(1)
                    .totalNum(4)
                    .price(35000)
                    .build();
            System.out.println(reservationRepository.save(newReservation));
        }
    }

    @Test
    public void testDelete() {
        String rid = "SAMPLE-RID-099";
        reservationRepository.deleteById(rid);
    }
}
