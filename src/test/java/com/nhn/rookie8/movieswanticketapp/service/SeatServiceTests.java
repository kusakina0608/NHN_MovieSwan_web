package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import com.nhn.rookie8.movieswanticketapp.repository.SeatRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;

@Log4j2
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class SeatServiceTests {
    @Spy
    private SeatRepository seatRepository = mock(SeatRepository.class);

    @InjectMocks
    private SeatService seatService = new SeatServiceImpl(seatRepository);

    private SeatDTO seatDTO;
    private String timetableId;
    private String reservationId;
    private String memberId1;
    private String seatCode;
    private String memberId2;

    @BeforeAll
    void beforeAllTest() {
        log.info("before all");
        timetableId = "aaa20210225";
        reservationId = "A924-A924-A924-A924";
        memberId1 = "saddummy";
        memberId2 = "hyerin9177";
        seatCode = "A01";
        seatDTO = SeatDTO.builder()
                .timetableId(timetableId)
                .reservationId(reservationId)
                .memberId(memberId1)
                .seatCode(seatCode)
                .build();
    }

    @BeforeEach
    void beforeEachTest(){
        log.info("before each");
    }

    @Test
    @Order(1)
    void preemptTest1(){ // 좌석이 선점되지 않은 경우
        SeatId seatId = SeatId.builder().timetableId(timetableId).seatCode(seatCode).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.empty();
        when(seatRepository.findById(seatId)).thenReturn(result);
//        Assertions.assertEquals(true, seatService.preemptSeat(seatDTO));
    }

    @Test
    @Order(2)
    void preemptTest2(){ // 내가 선점하고 있는 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(timetableId).seatCode(seatCode).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(memberId1);
//        Assertions.assertEquals(true, seatService.preemptSeat(seatDTO));
    }

    @Test
    @Order(3)
    void preemptTest3(){ // 다른 사람이 선점하고 있는 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(timetableId).seatCode(seatCode).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(memberId2);
//        Assertions.assertEquals(false, seatService.preemptSeat(seatDTO));
    }

    @Test
    @Order(4)
    void removeTest1(){ // 예약 또는 선점되지 않은 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(timetableId).seatCode(seatCode).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.empty();
        when(seatRepository.findById(seatId)).thenReturn(result);
        Assertions.assertEquals(false, seatService.cancelSeat(seatDTO));
    }

    @Test
    @Order(5)
    void removeTest2(){ // 내가 소유한 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(timetableId).seatCode(seatCode).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(memberId1);
        Assertions.assertEquals(true, seatService.cancelSeat(seatDTO));
    }

    @Test
    @Order(6)
    void removeTest3(){ // 다른 사람이 소유한 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(timetableId).seatCode(seatCode).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(memberId2);
        Assertions.assertEquals(false, seatService.cancelSeat(seatDTO));
    }

    @AfterAll
    void afterAllTest(){
        log.info("after all");
    }
}
