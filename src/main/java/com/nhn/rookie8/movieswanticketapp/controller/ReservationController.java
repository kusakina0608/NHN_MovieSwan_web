package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.dto.DiscountDTO;
import com.nhn.rookie8.movieswanticketapp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReservationController {
    private final MovieService movieService;
    private final TimetableService timetableService;
    private final ReservationService reservationService;
    private final SeatService seatService;
    private final MemberService memberService;

    @GetMapping("")
    public String reserve(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/reservation";
    }

    @PostMapping("/seat")
    public String seat(@RequestParam String movieId, @RequestParam String timetableId, Model model) {
        MovieDTO movieDTO = movieService.getMovie(movieId);
        TimetableDTO timetableDTO = timetableService.getTimetable(timetableId);
        List<String> reservedSeatList = seatService.getReservedSeatList(timetableDTO.getTimetableId());

        DiscountDTO discountDTO;
        if(timetableDTO.getStartTime().getHour() < 9){
            discountDTO = DiscountDTO.builder().discountRatio(0.25).discountType("조조 할인(오전 09:00 이전)").build();
        }
        else{
            discountDTO = DiscountDTO.builder().discountRatio(0.0).discountType("없음").build();
        }

        log.debug("좌석 선택 페이지입니다.");
        log.debug("movieDTO:\n{}\n", movieDTO.toString());
        log.debug("timetableDTO:\n{}\n", timetableDTO.toString());
        log.debug("discountDTO:\n{}\n", discountDTO.toString());
        log.debug("reservedSeatList:\n{}\n", reservedSeatList.toString());

        model.addAttribute("timetableDTO", timetableDTO);
        model.addAttribute("movieDTO", movieDTO);
        model.addAttribute("discountDTO", discountDTO);
        model.addAttribute("reservedSeatList", reservedSeatList);
        model.addAttribute("theater", "무비스완 판교점");
        return "page/seat";
    }

    @PostMapping("/pay")
    public String pay(
            HttpServletRequest httpServletRequest,
            @ModelAttribute MovieDTO movieDTO,
            @ModelAttribute TimetableDTO timetableDTO,
            @ModelAttribute DiscountDTO discountDTO,
            @ModelAttribute ReservationDTO reservationDTO,
            @RequestParam String seats,
            Model model
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        MemberDTO memberDTO = memberService.getMemberInfoById((String) session.getAttribute("memberId"));
        reservationDTO.setMemberId(memberDTO.getMemberId());
        movieDTO = movieService.getMovie(movieDTO.getMovieId());
        timetableDTO = timetableService.getTimetable(timetableDTO.getTimetableId());

        log.debug("결제 페이지입니다.");
        log.debug("movieDTO: {}", movieDTO);
        log.debug("timetableDTO: {}", timetableDTO);
        log.debug("discountDTO: {}", discountDTO);
        log.debug("reservationDTO: {}", reservationDTO);
        log.debug("seats: {}", seats);

        model.addAttribute("movieDTO", movieDTO);
        model.addAttribute("timetableDTO", timetableDTO);
        model.addAttribute("discountDTO", discountDTO);
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("seats", seats);
        return "page/pay";
    }

    @PostMapping("/result")
    public String reservationResult(
            HttpServletRequest httpServletRequest,
            @ModelAttribute MovieDTO movieDTO,
            @ModelAttribute TimetableDTO timetableDTO,
            @ModelAttribute DiscountDTO discountDTO,
            @ModelAttribute ReservationDTO reservationDTO,
            @RequestParam String seats,
            Model model
    ) {
        HttpSession session = httpServletRequest.getSession(false);
        MemberDTO memberDTO = memberService.getMemberInfoById((String) session.getAttribute("memberId"));
        reservationDTO.setMemberId(memberDTO.getMemberId());
        movieDTO = movieService.getMovie(movieDTO.getMovieId());
        timetableDTO = timetableService.getTimetable(timetableDTO.getTimetableId());

        log.debug("예매 완료 페이지입니다.");
        log.debug("movieDTO: {}", movieDTO);
        log.debug("timetableDTO: {}", timetableDTO);
        log.debug("discountDTO: {}", discountDTO);
        log.debug("reservationDTO: {}", reservationDTO);
        log.debug("seats: {}", seats);

        reservationDTO = reservationService.reserve(reservationDTO);
        log.debug("reservationDTO: {}", reservationDTO);
        seatService.confirmSeat(
                timetableDTO.getTimetableId(),
                memberDTO.getMemberId(),
                reservationDTO.getReservationId(),
                seats
        );

        model.addAttribute("movieDTO", movieDTO);
        model.addAttribute("timetableDTO", timetableDTO);
        model.addAttribute("discountDTO", discountDTO);
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("seats", seats);

        return "page/reservation_result";
    }
}
