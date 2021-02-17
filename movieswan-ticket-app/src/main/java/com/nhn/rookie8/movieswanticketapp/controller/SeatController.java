package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.service.ReservationService;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/seat")
@Log4j2
@RequiredArgsConstructor
@RestController
public class SeatController {
    private final SeatService service;

    @PostMapping("/preempt")
    public boolean preemptSeat(@RequestParam String tid, @RequestParam String sid) {
        // TODO: 내 아이디를 불러오는 방식으로 변경
        String uid = "kusakina0608";
        SeatDTO seatDTO = SeatDTO.builder()
                .tid(tid)
                .sid(sid)
                .uid(uid)
                .rid(null)
                .build();
        boolean result = false;
        try{
            result = service.preempt(seatDTO);
        }
        catch(DataIntegrityViolationException e){
            System.out.println("찰나의 순간 자리를 뺏겼습니다. 유감ㅎ");
            result = false;
        }
        finally{
            System.out.println("그럼 이만...");
            return result;
        }
    }
}
