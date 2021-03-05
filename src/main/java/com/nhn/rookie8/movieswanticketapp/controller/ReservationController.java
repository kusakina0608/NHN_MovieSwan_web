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
import javax.transaction.Transactional;
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
    private final WebHook webHook;

    @GetMapping("")
    public String reserve(Model model) {
        List<MovieDTO> movieList = movieService.getScheduledMovieList();
        model.addAttribute("movieList", movieList);
        return "page/reservation";
    }

    @PostMapping("/seat")
    public String seat(
            @RequestParam String timetableId,
            Model model
    ) {
        TimetableDTO timetableDTO = timetableService.getTimetable(timetableId);
        MovieDTO movieDTO = movieService.getMovieDetail(timetableDTO.getMovieId());
        DiscountDTO discountDTO = new DiscountDTO(timetableDTO.getStartTime().getHour());

        model.addAttribute("timetableDTO", timetableDTO);
        model.addAttribute("movieDTO", movieDTO);
        model.addAttribute("discountDTO", discountDTO);
        model.addAttribute("seats", seatService.getAllSeat(timetableId, 13, 18));
        model.addAttribute("theater", "무비스완 판교점");
        return "page/seat";
    }

    @PostMapping("/pay")
    public String pay(
            HttpServletRequest request,
            @ModelAttribute ReservationDTO reservationDTO,
            @RequestParam String seats,
            Model model
    ) {
        MemberDTO memberDTO = memberService.getMemberInfoById((String)request.getAttribute("memberId"));
        reservationDTO.setMemberId(memberDTO.getMemberId());
        TimetableDTO timetableDTO = timetableService.getTimetable(reservationDTO.getTimetableId());
        MovieDTO movieDTO = movieService.getMovieDetail(timetableDTO.getMovieId());
        DiscountDTO discountDTO = new DiscountDTO(timetableDTO.getStartTime().getHour());

        model.addAttribute("movieDTO", movieDTO);
        model.addAttribute("timetableDTO", timetableDTO);
        model.addAttribute("discountDTO", discountDTO);
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("seats", seats);
        return "page/pay";
    }

    @Transactional
    @PostMapping("/result")
    public String reservationResult(
            HttpServletRequest request,
            @ModelAttribute ReservationDTO reservationDTO,
            @RequestParam String seats,
            Model model
    ) {
        MemberDTO memberDTO = memberService.getMemberInfoById((String)request.getAttribute("memberId"));
        reservationDTO.setMemberId(memberDTO.getMemberId());
        TimetableDTO timetableDTO = timetableService.getTimetable(reservationDTO.getTimetableId());
        MovieDTO movieDTO = movieService.getMovieDetail(timetableDTO.getMovieId());

        reservationDTO = reservationService.reserve(reservationDTO);

        seatService.confirmSeat(
                timetableDTO.getTimetableId(),
                memberDTO.getMemberId(),
                reservationDTO.getReservationId(),
                seats
        );

        webHook.sendReservationSuccessMessage(memberDTO, movieDTO, reservationDTO);

        model.addAttribute("movieDTO", movieDTO);
        model.addAttribute("timetableDTO", timetableDTO);
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("seats", seats);
        return "page/reservation_result";
    }
}
