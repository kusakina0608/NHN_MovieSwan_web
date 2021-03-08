package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.SeatDTO;
import com.nhn.rookie8.movieswanticketapp.service.MemberService;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/seat")
@Log4j2
@RequiredArgsConstructor
@RestController
public class SeatController {
    private final SeatService seatService;
    private final MemberService memberService;

    @GetMapping("/list")
    public List<String> getReservedSeatList(String timetableId) {
        return seatService.getReservedSeatList(timetableId);
    }

    @PostMapping("/preempt")
    public boolean preemptSeat(
            HttpServletRequest request,
            @RequestParam String timetableId,
            @RequestParam String seatCode) {
        MemberDTO memberDTO = memberService.getMemberInfoById((String)request.getAttribute("memberId"));
        log.info("{} 사용자의 좌석 선점 요청. 상영번호: {}, 좌석번호: {}", memberDTO.getMemberId(), timetableId, seatCode);
        SeatDTO seatDTO = SeatDTO.builder()
                .timetableId(timetableId)
                .seatCode(seatCode)
                .memberId(memberDTO.getMemberId())
                .reservationId(null)
                .build();
        boolean result = false;
        try{
            result = seatService.preempt(seatDTO);
            log.info("{} 사용자의 좌석 선점 성공. 상영번호: {}, 좌석번호: {}", memberDTO.getMemberId(), timetableId, seatCode);
        }
        catch(DataIntegrityViolationException e){
            log.info("{} 사용자의 좌석 선점 실패. 상영번호: {}, 좌석번호: {}", memberDTO.getMemberId(), timetableId, seatCode);
            result = false;
        }
        return result;
    }

    @DeleteMapping("/preempt")
    public boolean cancelSeat(HttpServletRequest request, @RequestParam String timetableId, @RequestParam String seatCode) {
        MemberDTO memberDTO = memberService.getMemberInfoById((String)request.getAttribute("memberId"));
        SeatDTO seatDTO = SeatDTO.builder()
                .timetableId(timetableId)
                .seatCode(seatCode)
                .memberId(memberDTO.getMemberId())
                .reservationId(null)
                .build();
        boolean result = false;
        result = seatService.remove(seatDTO);
        return result;
    }
}
