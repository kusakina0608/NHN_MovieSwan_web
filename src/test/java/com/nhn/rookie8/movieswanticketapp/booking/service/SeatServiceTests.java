package com.nhn.rookie8.movieswanticketapp.booking.service;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SeatServiceTests {

    @Autowired
    private SeatService service;

    @Test
    public void testRegister() {
        SeatDTO seatDTO1 = SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("F08")
                .uid("kusakina0608")
                .rid("A924-A000-A025")
                .build();
        SeatDTO seatDTO2 = SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("F09")
                .uid("hyerin9177")
                .rid(null)
                .build();

        System.out.println(service.register(seatDTO1));
        System.out.println(service.register(seatDTO2));
    }

    @Test
    public void testGetReservedSeatList(){
        service.getReservedSeatList("AAA2102251900").forEach(seat -> {
            System.out.println(seat);
        });
    }

    @Test
    public void testPreempt1() { // 빈 좌석 선택시
        SeatDTO seatDTO = SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("A60")
                .uid("kusakina0608")
                .rid(null)
                .build();
        try{
            System.out.println(service.preempt(seatDTO));
        }
        catch(DataIntegrityViolationException e){
            System.out.println("여기서 에러가 잡혔다...");
        }
        finally{
            System.out.println("끝...");
        }
    }

    @Test
    public void testPreempt2() { // 내가 선점한 좌석 선택시
        SeatDTO seatDTO = SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("F08")
                .uid("kusakina0608")
                .rid(null)
                .build();
        System.out.println(service.preempt(seatDTO));
    }

    @Test
    public void testPreempt3() { // 남이 선점한 좌석 선택시
        SeatDTO seatDTO = SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("F09")
                .uid("kusakina0608")
                .rid(null)
                .build();
        System.out.println(service.preempt(seatDTO));
    }

    @Test
    public void testModify(){
        List<SeatDTO> dtoList= new ArrayList<>();
        dtoList.add(SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("A01")
                .rid("TESTTEST")
                .uid("kusakina0608")
                .build());
        dtoList.add(SeatDTO.builder()
                .tid("AAA2102251230")
                .sid("A02")
                .rid("TESTTEST")
                .uid("kusakina0608")
                .build());
        service.modify(dtoList, "TEST-RID");
        dtoList.forEach(dto -> {
            service.register(dto);
        });
    }

    @Test
    public void testGetMySeatList(){
        List<String> sidList = service.getMySeatList("W16M-FA4B-43D7-VIIS");
        sidList.forEach(sid -> {
            System.out.println(sid);
        });
    }
}
