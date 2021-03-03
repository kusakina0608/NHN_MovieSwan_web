package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/")
@RequiredArgsConstructor
public class PageController {

    private final MovieService movieService;
    private final ReservationService reservationService;
    private final SeatService seatService;
    private final ReviewService reviewService;
    private final QuestionService questionService;
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    @Value("${accountURL}")
    private String accountUrl;

    @GetMapping({"/", "/main"})
    public String mainPage(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);

        if (!(session == null || session.getAttribute("member") == null)) {
            log.info(session.getAttribute("member"));

            model.addAttribute("member", session.getAttribute("member"));
        }
        return "page/main_page";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<MovieDTO> movieList = movieService.getCurrentMovieList();
        model.addAttribute("movieList", movieList);
        return "page/admin_page";
    }

    // 여기부터 전부 마이페이지 입니다...
    @GetMapping("/mypage")
    public String myPage() {
        return "redirect:/mypage/userinfo";
    }

    @GetMapping("/mypage/userinfo")
    public String myPageUserinfo(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        MemberDTO memberDTO = memberService.getUserInfoById((String) session.getAttribute("memberId"));

        model.addAttribute("regDate", memberDTO.getRegDate().toString().split("T")[0]);
        model.addAttribute("memberId", memberDTO.getMemberId());
        model.addAttribute("name", memberDTO.getName());
        model.addAttribute("email", memberDTO.getEmail());
        model.addAttribute("url", memberDTO.getUrl());
        return "page/my_page_userinfo";
    }

    @GetMapping("/mypage/ticket")
    public String myTicketPage(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);

        model.addAttribute("result", reservationService.getMyReservationList(pageRequestDTO, (String) session.getAttribute("memberId")));
        return "page/my_page_ticket";
    }

    @GetMapping("/mypage/ticket/detail")
    public String myTicketDetailPage(@RequestParam String reservationId, Model model) {
        List<String> result = seatService.getMySeatList(reservationId);
        StringBuilder seat = new StringBuilder();
        result.forEach(s -> seat.append(s).append(' '));

        model.addAttribute("seat", seat.toString());
        model.addAttribute("result", reservationService.getReservation(reservationId));
        return "page/my_page_ticket_detail";
    }

    @GetMapping("/mypage/ticket/delete")
    public String myPageTicketDelete(@RequestParam String rid) {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .reservationId(rid)
                .build();
        reservationService.delete(reservationDTO);
        return "redirect:/mypage/ticket";
    }

    @GetMapping("/mypage/movie")
    public String myPageMyMovie(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        String memberId = session.getAttribute("memberId").toString();
        List<String> movieIdList = favoriteService.getFavoriteList(memberId);
        PageResultDTO<MovieDTO, Movie> result = movieService.getListByMovieId(pageRequestDTO, movieIdList);

        model.addAttribute("result", result);
        return "page/my_page_mymovie";
    }

    @GetMapping("/mypage/review")
    public String myPageMyReview(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        String memberId = session.getAttribute("memberId").toString();
        PageResultDTO<ReviewDTO, Review> resultDTO = reviewService.findMyReviews(pageRequestDTO, memberId);

        model.addAttribute("result", resultDTO);
        return "page/my_page_myreview";
    }

    @GetMapping("/mypage/question")
    public String myPageQuestion(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        String memberId = (String) session.getAttribute("memberId");

        model.addAttribute("result", questionService.getMyQuestionList(pageRequestDTO, memberId));
        return "page/my_page_question";
    }

    @GetMapping("/mypage/question/register")
    public String questionPage(HttpServletRequest httpServletRequest, Model model) {
        return "page/question_page";
    }

    @GetMapping("/mypage/question/post")
    public String myPageReadQuestion(@RequestParam("questionId") Integer questionId, Model model) {
        model.addAttribute("dto", questionService.readQuestion(questionId));
        return "page/my_page_read_question";
    }
}
