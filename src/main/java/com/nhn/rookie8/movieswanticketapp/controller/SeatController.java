package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/api/seat")
@Log4j2
@RequiredArgsConstructor
@RestController
public class SeatController {
    private final SeatService service;

    @GetMapping("/list")
    public List<String> getReservedSeatList(String tid) {
        return service.getReservedSeatList(tid);
    }

    @PostMapping("/preempt")
    public boolean preemptSeat(HttpServletRequest httpServletRequest, @RequestParam String tid, @RequestParam String sid) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return false;
        }
        String uid = (String)session.getAttribute("uid");
        log.info("{} 사용자의 좌석 선점 요청. 상영번호: {}, 좌석번호: {}", uid, tid, sid);
        SeatDTO seatDTO = SeatDTO.builder()
                .tid(tid)
                .sid(sid)
                .uid(uid)
                .rid(null)
                .build();
        boolean result = false;
        try{
            result = service.preempt(seatDTO);
            log.info("{} 사용자의 좌석 선점 성공. 상영번호: {}, 좌석번호: {}", uid, tid, sid);
        }
        catch(DataIntegrityViolationException e){
            log.info("{} 사용자의 좌석 선점 실패. 상영번호: {}, 좌석번호: {}", uid, tid, sid);
            result = false;
        }
        return result;
    }

    @DeleteMapping("/preempt")
    public boolean cancelSeat(HttpServletRequest httpServletRequest, @RequestParam String tid, @RequestParam String sid) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return false;
        }
        String uid = (String)session.getAttribute("uid");
        SeatDTO seatDTO = SeatDTO.builder()
                .tid(tid)
                .sid(sid)
                .uid(uid)
                .rid(null)
                .build();
        boolean result = false;
        result = service.remove(seatDTO);
        return result;
    }
}
