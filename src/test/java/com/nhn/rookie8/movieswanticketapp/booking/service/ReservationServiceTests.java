package com.nhn.rookie8.movieswanticketapp.booking.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
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

    @Test
    public void testDelete(){
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .rid("1DVI-JME4-IZC3-584C")
                .build();
        System.out.println(service.delete(reservationDTO));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<ReservationDTO, Reservation> resultDTO = service.getMyReservationList(pageRequestDTO, "");
        for(ReservationDTO reservationDTO : resultDTO.getDtoList()) {
            System.out.println(reservationDTO);
        }
    }
}
