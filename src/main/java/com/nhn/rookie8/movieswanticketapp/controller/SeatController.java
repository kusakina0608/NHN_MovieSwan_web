package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.dto.UserDTO;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import com.nhn.rookie8.movieswanticketapp.service.UserService;
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
    private final SeatService seatService;
    private final UserService userService;

    @GetMapping("/list")
    public List<String> getReservedSeatList(String tid) {
        return seatService.getReservedSeatList(tid);
    }

    @PostMapping("/preempt")
    public boolean preemptSeat(
            HttpServletRequest httpServletRequest,
            @RequestParam String timetableId,
            @RequestParam String seatCode) {
        HttpSession session = httpServletRequest.getSession(false);
        UserDTO userDTO = userService.getUserInfoById((String) session.getAttribute("uid"));
        log.info("{} 사용자의 좌석 선점 요청. 상영번호: {}, 좌석번호: {}", userDTO.getUid(), timetableId, seatCode);
        SeatDTO seatDTO = SeatDTO.builder()
                .timetableId(timetableId)
                .seatCode(seatCode)
                .memberId(userDTO.getUid())
                .reservationId(null)
                .build();
        boolean result = false;
        try{
            result = seatService.preempt(seatDTO);
            log.info("{} 사용자의 좌석 선점 성공. 상영번호: {}, 좌석번호: {}", userDTO.getUid(), timetableId, seatCode);
        }
        catch(DataIntegrityViolationException e){
            log.info("{} 사용자의 좌석 선점 실패. 상영번호: {}, 좌석번호: {}", userDTO.getUid(), timetableId, seatCode);
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
                .timetableId(tid)
                .seatCode(sid)
                .memberId(uid)
                .reservationId(null)
                .build();
        boolean result = false;
        result = seatService.remove(seatDTO);
        return result;
    }
}
