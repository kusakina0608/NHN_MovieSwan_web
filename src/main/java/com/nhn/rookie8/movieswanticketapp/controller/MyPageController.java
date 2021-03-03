package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final ReservationService reservationService;
    private final QuestionService questionService;
    private final MemberService memberService;
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;
    private final MovieService movieService;
    private final SeatService seatService;

    @GetMapping({"/", ""})
    public String myPage() {
        return "redirect:/mypage/memberinfo";
    }


    @GetMapping("/memberinfo")
    public String myPageMemberinfo(HttpServletRequest request, Model model) {

        MemberDTO memberDTO = memberService
                .getMemberInfoById(request.getAttribute("memberId").toString());

        model.addAttribute("member", memberDTO);

        return "page/my_page_memberinfo";
    }


    @GetMapping("/ticket")
    public String myTicketPage(PageRequestDTO pageRequestDTO, HttpServletRequest request, Model model) {

        model.addAttribute("result",
                reservationService.getMyReservationList(
                        pageRequestDTO, request.getAttribute("memberId").toString()
                )
        );

        return "page/my_page_ticket";
    }

    @GetMapping("/ticket/detail")
    public String myTicketDetailPage(@RequestParam String reservationId, Model model) {
        List<String> result = seatService.getMySeatList(reservationId);
        StringBuilder seat = new StringBuilder();
        result.forEach(s -> seat.append(s).append(' '));

        model.addAttribute("seat", seat.toString());
        model.addAttribute("result", reservationService.getReservation(reservationId));
        return "page/my_page_ticket_detail";
    }

    @GetMapping("/ticket/delete")
    public String myPageTicketDelete(@RequestParam String reservationId) {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .reservationId(reservationId)
                .build();
        reservationService.delete(reservationDTO);
        return "redirect:/mypage/ticket";
    }

    @GetMapping("/movie")
    public String myPageMyMovie(PageRequestDTO pageRequestDTO, HttpServletRequest request, Model model) {
        List<String> movieIdList = favoriteService.getFavoriteList(request.getAttribute("memberId").toString());
        PageResultDTO<MovieDTO, Movie> result = movieService.getListByMovieId(pageRequestDTO, movieIdList);
        model.addAttribute("result", result);
        return "page/my_page_mymovie";
    }

    @GetMapping("/review")
    public String myPageMyReview(PageRequestDTO pageRequestDTO, HttpServletRequest request, Model model) {
        PageResultDTO<ReviewDTO, Review> resultDTO =
                reviewService.findMyReviews(pageRequestDTO, request.getAttribute("memberId").toString());
        model.addAttribute("result", resultDTO);
        return "page/my_page_myreview";
    }

    @GetMapping("/question")
    public String myPageQuestion(PageRequestDTO pageRequestDTO, HttpServletRequest request, Model model) {

        model.addAttribute("result",
                questionService.getMyQuestionList(pageRequestDTO, request.getAttribute("memberId").toString()));
        return "page/my_page_question";
    }

    @GetMapping("/question/register")
    public String questionPage(HttpServletRequest httpServletRequest, Model model) {
        return "page/question_page";
    }

    @GetMapping("/question/post")
    public String myPageReadQuestion(@RequestParam("questionId") Integer questionId, Model model) {
        model.addAttribute("dto", questionService.readQuestion(questionId));
        return "page/my_page_read_question";
    }
}
