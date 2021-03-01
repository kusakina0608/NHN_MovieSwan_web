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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private String tid;
    private String rid;
    private String uid1;
    private String sid;
    private String uid2;

    @BeforeAll
    void beforeAllTest() {
        log.info("before all");
        tid = "aaa20210225";
        rid = "A924-A924-A924-A924";
        uid1 = "saddummy";
        uid2 = "hyerin9177";
        sid = "A01";
        seatDTO = SeatDTO.builder()
                .timetableId(tid)
                .reservationId(rid)
                .memberId(uid1)
                .seatCode(sid)
                .build();
    }

    @BeforeEach
    void beforeEachTest(){
        log.info("before each");
    }

    @Test
    @Order(1)
    void registerTest(){
        Seat entity = seatService.dtoToEntity(seatDTO);
        when(seatRepository.save(entity)).thenReturn(entity);
        Assertions.assertEquals("A01", seatService.register(seatDTO));
        log.info("first");
    }

    @Test
    @Order(2)
    void preemptTest1(){ // 좌석이 선점되지 않은 경우
        SeatId seatId = SeatId.builder().timetableId(tid).seatCode(sid).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.empty();
        when(seatRepository.findById(seatId)).thenReturn(result);
        Assertions.assertEquals(true, seatService.preempt(seatDTO));
    }

    @Test
    @Order(3)
    void preemptTest2(){ // 내가 선점하고 있는 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(tid).seatCode(sid).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(uid1);
        Assertions.assertEquals(true, seatService.preempt(seatDTO));
    }

    @Test
    @Order(4)
    void preemptTest3(){ // 다른 사람이 선점하고 있는 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(tid).seatCode(sid).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(uid2);
        Assertions.assertEquals(false, seatService.preempt(seatDTO));
    }

    @Test
    @Order(5)
    void removeTest1(){ // 예약 또는 선점되지 않은 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(tid).seatCode(sid).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.empty();
        when(seatRepository.findById(seatId)).thenReturn(result);
        Assertions.assertEquals(false, seatService.remove(seatDTO));
    }

    @Test
    @Order(6)
    void removeTest2(){ // 내가 소유한 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(tid).seatCode(sid).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(uid1);
        Assertions.assertEquals(true, seatService.remove(seatDTO));
    }

    @Test
    @Order(7)
    void removeTest3(){ // 다른 사람이 소유한 좌석인 경우
        SeatId seatId = SeatId.builder().timetableId(tid).seatCode(sid).build();
        Seat seat = mock(Seat.class);
        Optional<Seat> result = Optional.of(seat);
        when(seatRepository.findById(seatId)).thenReturn(result);
        when(seat.getMemberId()).thenReturn(uid2);
        Assertions.assertEquals(false, seatService.remove(seatDTO));
    }

    @AfterAll
    void afterAllTest(){
        log.info("after all");
    }
}
