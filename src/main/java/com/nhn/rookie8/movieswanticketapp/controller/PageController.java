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
    private final UserService userService;

    @Value("${accountURL}")
    private String accountUrl;

    @GetMapping({"/", "/main"})
    public String mainPage(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);

        if (!(session == null || session.getAttribute("uid") == null)) {
            model.addAttribute("uid", session.getAttribute("uid"));
            model.addAttribute("name", session.getAttribute("name"));
        }
        return "page/main_page";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/admin_page";
    }

    @GetMapping("/movie")
    public String moviePage() {
        return "redirect:/movie/current/list";
    }

    @GetMapping("/movie/current/list")
    public String currentMovieList(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getList(pageRequestDTO, true);

        HttpSession session = httpServletRequest.getSession(false);
        if (!(session == null || session.getAttribute("uid") == null))
            model.addAttribute("uid", session.getAttribute("uid"));

        model.addAttribute("result", resultDTO);
        model.addAttribute("current", true);
        return "/page/movie_list";
    }

    @GetMapping("/movie/expected/list")
    public String expectedMovieList(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getList(pageRequestDTO, false);

        HttpSession session = httpServletRequest.getSession(false);
        if (!(session == null || session.getAttribute("uid") == null))
            model.addAttribute("uid", session.getAttribute("uid"));

        model.addAttribute("result", resultDTO);
        model.addAttribute("current", false);
        return "/page/movie_list";
    }

    @GetMapping("/movie/detail")
    public String movieDetail(String mid, PageRequestDTO reviewRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        MovieDTO movieDTO = movieService.getMovie(mid);

        HttpSession session = httpServletRequest.getSession(false);
        String uid;
        if (!(session == null || session.getAttribute("uid") == null)) {
            model.addAttribute("uid", session.getAttribute("uid"));
            uid = session.getAttribute("uid").toString();
        }
        else
            uid = "";
        
        model.addAttribute("dto", movieDTO);
        model.addAttribute("reviews", reviewService.getList(reviewRequestDTO, mid));
        model.addAttribute("my_review", reviewService.findMyReviewByMid(mid, uid));
        return "/page/movie_detail";
    }

    @GetMapping("/mypage/question/register")
    public String questionPage(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("uid", session.getAttribute("uid"));
            return "page/question_page";
        }
    }

    // 여기부터 전부 마이페이지 입니다...
    @GetMapping("/mypage")
    public String myPage() {
        return "redirect:/mypage/userinfo";
    }

    @GetMapping("/mypage/userinfo")
    public String myPageUserinfo(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        UserDTO userDTO = userService.getUserInfoById((String) session.getAttribute("uid"));

        model.addAttribute("regDate", userDTO.getRegDate().toString().split("T")[0]);
        model.addAttribute("uid", userDTO.getUid());
        model.addAttribute("name", userDTO.getName());
        model.addAttribute("email", userDTO.getEmail());
        model.addAttribute("url", userDTO.getUrl());
        return "page/my_page_userinfo";
    }

    @GetMapping("/mypage/ticket")
    public String myTicketPage(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);

        model.addAttribute("result", reservationService.getMyReservationList(pageRequestDTO, (String) session.getAttribute("uid")));
        return "page/my_page_ticket";
    }

    @GetMapping("/mypage/ticket/detail")
    public String myTicketDetailPage(@RequestParam String rid, Model model) {
        List<String> result = seatService.getMySeatList(rid);
        StringBuilder seat = new StringBuilder();
        result.forEach(s -> seat.append(s).append(' '));

        model.addAttribute("seat", seat.toString());
        model.addAttribute("result", reservationService.getReservation(rid));
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
        String uid = session.getAttribute("uid").toString();
        List<String> midList = favoriteService.getList(uid);
        PageResultDTO<MovieDTO, Movie> result = movieService.getListByMid(pageRequestDTO, midList);

        model.addAttribute("result", result);
        return "page/my_page_mymovie";
    }

    @GetMapping("/mypage/review")
    public String myPageMyReview(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        String uid = session.getAttribute("uid").toString();
        PageResultDTO<ReviewDTO, Review> resultDTO = reviewService.findMyReviews(pageRequestDTO, uid);

        model.addAttribute("result", resultDTO);
        return "page/my_page_myreview";
    }

    @GetMapping("/mypage/question")
    public String myPageQuestion(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        String uid = (String) session.getAttribute("uid");

        model.addAttribute("result", questionService.getMyQuestionList(pageRequestDTO, uid));
        return "page/my_page_question";
    }

    @GetMapping("/mypage/question/post")
    public String myPageReadQuestion(@RequestParam("qid") Integer qid, Model model) {
        model.addAttribute("dto", questionService.readQuestion(qid));
        return "page/my_page_read_question";
    }
}
