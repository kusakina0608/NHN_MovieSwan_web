package com.nhn.rookie8.movieswanticketapp.booking.service;

import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReservationServiceTests {

    @Autowired
    private ReservationService service;

    @Test
    public void testRegister() {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .rid("SAMPLE-RID-777")
                .tid("SAMPLE-RID-777")
                .uid("SAMPLE-RID-777")
                .childNum(3)
                .adultNum(4)
                .oldNum(5)
                .totalNum(12)
                .price(80000)
                .build();

        System.out.println(service.register(reservationDTO));
    }
}
