package com.nhn.rookie8.movieswanticketapp.booking.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SeatServiceTests {

    @Autowired
    private SeatService service;

    @Test
    public void testRegister() {
        SeatDTO seatDTO = SeatDTO.builder()
                .rid("SAMPLE-RID-777")
                .sid("A924")
                .build();

        System.out.println(service.register(seatDTO));
    }
}
