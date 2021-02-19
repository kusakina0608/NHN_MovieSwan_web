package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import com.nhn.rookie8.movieswanticketapp.service.ReservationService;
import com.nhn.rookie8.movieswanticketapp.service.SeatService;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PageController {
    private final MovieService movieService;
    private final ReservationService reservationService;
    private final SeatService seatService;
    private final ReviewService reviewService;
    private final QuestionService questionService;

    @GetMapping({"/", "/main"})
    public String main_page(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (!(session == null || session.getAttribute("uid") == null)) {
            model.addAttribute("uid", session.getAttribute("uid"));
            model.addAttribute("name", session.getAttribute("name"));
        }
        return "page/main_page";
    }

    @GetMapping("/admin")
    public String admin_page(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/admin_page";
    }

    @GetMapping("/movie")
    public String movie_page() {
        return "redirect:/movie/current/list";
    }

    @GetMapping("/movie/current/list")
    public String currentMovieList(PageRequestDTO pageRequestDTO, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getList(pageRequestDTO, true);
        List<MovieDTO> movieList = resultDTO.getDtoList();
        HashMap<String, String> gradeMap = new HashMap<String, String>();
        movieList.forEach(movieDTO -> {
             float grade = reviewService.getGradeByMid(movieDTO.getMid());
             gradeMap.put(movieDTO.getMid(), String.format("%.1f", grade));
        });

        model.addAttribute("result", resultDTO);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("current", true);
        return "/page/movie_list";
    }

    @GetMapping("/movie/expected/list")
    public String expectedMovieList(PageRequestDTO pageRequestDTO, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getList(pageRequestDTO, false);
        List<MovieDTO> movieList = resultDTO.getDtoList();
        HashMap<String, String> gradeMap = new HashMap<String, String>();
        movieList.forEach(movieDTO -> {
            float grade = reviewService.getGradeByMid(movieDTO.getMid());
            gradeMap.put(movieDTO.getMid(), String.format("%.1f", grade));
        });

        model.addAttribute("result", resultDTO);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("current", false);
        return "/page/movie_list";
    }

    @GetMapping("/movie/detail")
    public String movieDetail(String mid, PageRequestDTO reviewRequestDTO, Model model) {
        MovieDTO movieDTO = movieService.read(mid);
        //TODO uid를 받아서 구현
        String uid = "testuser";

        model.addAttribute("dto", movieDTO);
        model.addAttribute("reviews", reviewService.getList(reviewRequestDTO, mid));
        model.addAttribute("my_review", reviewService.findMyReview(mid, uid));
        return "/page/movie_detail";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/booking";
    }

    @PostMapping("/booking/seat")
    public String seat(@RequestParam("mid") String mid, @RequestParam("date") String date, @RequestParam("time") String time, @RequestParam("tid") String tid, Model model) {
        System.out.println(mid);
        System.out.println(date);
        System.out.println(time);
        System.out.println(tid);
        // TODO: mid로 영화 조회
        MovieDTO movieDTO = movieService.read(mid);
        model.addAttribute("title", movieDTO.getName());
        model.addAttribute("poster", movieDTO.getPoster());
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("tid", tid);
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        if(Integer.parseInt(time.split(":")[0]) < 9){
            model.addAttribute("discount", "조조 할인(오전 09:00 이전)");
        }
        else{
            model.addAttribute("discount", "없음");
        }
        model.addAttribute("seatlist", seatService.getReservedSeatList(tid));
        return "page/seat";
    }

    @PostMapping("/booking/pay")
    public String pay(@RequestParam HashMap<String,String> params, Model model) {
        params.keySet().forEach(key -> {
            model.addAttribute(key, params.get(key));
        });
        return "page/pay";
    }

    @PostMapping("/booking/result")
    public String bookingResult(@RequestParam HashMap<String,String> params, Model model) {
        params.keySet().forEach(key -> {
            model.addAttribute(key, params.get(key));
            System.out.println(key + ": " +  params.get(key));
        });


        String randomId = reservationService.createReservationId();
        System.out.println(randomId);
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .rid(randomId)
                .tid(params.get("tid"))
                .uid("kusakina0608") // TODO: 아이디 불러와서 여기에 넣어주기~!
                .childNum(Integer.parseInt(params.get("childnum")))
                .adultNum(Integer.parseInt(params.get("adultnum")))
                .oldNum(Integer.parseInt(params.get("oldnum")))
                .totalNum(Integer.parseInt(params.get("totalnum")))
                .price(Integer.parseInt(params.get("price")))
                .build();

        reservationService.register(reservationDTO);

        List<SeatDTO> dtoList= new ArrayList<SeatDTO>();
        String[] seatList = params.get("seats").split(",");
        for (String seat : seatList) {
            dtoList.add(SeatDTO.builder()
                    .tid(params.get("tid"))
                    .sid(seat)
                    .rid(randomId)
                    .uid("kusakina0608") // TODO: 아이디 불러와서 여기에 넣어주기~!
                    .build());
        }
        seatService.modify(dtoList, randomId);

        return "page/booking_result";
    }

    @GetMapping("/question")
    public String question_page(HttpServletRequest httpServletRequest, Model model) {
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
    public String my_page() {
        return "redirect:/mypage/userinfo";
    }

    @GetMapping("/mypage/userinfo")
    public String my_page_userinfo(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else { return "page/my_page_userinfo"; }
    }

    @GetMapping("/mypage/ticket")
    public String my_page_ticket(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("result", reservationService.getMypageList(pageRequestDTO));
            return "page/my_page_ticket";
        }
    }

    @GetMapping("/mypage/ticket/detail")
    public String my_page_ticket_detail(@RequestParam String rid, PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("result", reservationService.getMypageList(pageRequestDTO));
            return "page/my_page_ticket_detail";
        }
    }

    @GetMapping("/mypage/movie")
    public String my_page_mymovie(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else { return "page/my_page_mymovie"; }
    }

    @GetMapping("/mypage/review")
    public String my_page_myreview(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else { return "page/my_page_myreview"; }
    }

    @GetMapping("/mypage/question")
    public String my_page_question(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("result", questionService.getQuestionList(pageRequestDTO));
            return "page/my_page_question";
        }
    }

    @GetMapping("/mypage/question/post")
    public String my_page_read_question(@RequestParam("qid") Integer qid, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("dto", questionService.readQuestion(qid));
            return "page/my_page_read_question";
        }
    }
}