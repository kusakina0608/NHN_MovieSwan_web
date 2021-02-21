package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.service.*;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
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
    public String currentMovieList(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getList(pageRequestDTO, true);
        List<MovieDTO> movieList = resultDTO.getDtoList();
        HashMap<String, String> gradeMap = new HashMap<String, String>();
        movieList.forEach(movieDTO -> {
             float grade = reviewService.getGradeByMid(movieDTO.getMid());
             gradeMap.put(movieDTO.getMid(), String.format("%.1f", grade));
        });

        HttpSession session = httpServletRequest.getSession(false);
        String uid;
        if (!(session == null || session.getAttribute("uid") == null)) {
            model.addAttribute("uid", session.getAttribute("uid"));
            uid = session.getAttribute("uid").toString();
        }
        else
            uid = "";

        HashMap<String, Boolean> favMap = new HashMap<String, Boolean>();
        movieList.forEach(movieDTO -> {
            boolean isFav = favoriteService.isFavorite(uid, movieDTO.getMid());
            favMap.put(movieDTO.getMid(), isFav);
        });

        model.addAttribute("result", resultDTO);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("favMap", favMap);
        model.addAttribute("current", true);
        return "/page/movie_list";
    }

    @GetMapping("/movie/expected/list")
    public String expectedMovieList(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getList(pageRequestDTO, false);
        List<MovieDTO> movieList = resultDTO.getDtoList();
        HashMap<String, String> gradeMap = new HashMap<String, String>();
        movieList.forEach(movieDTO -> {
            float grade = reviewService.getGradeByMid(movieDTO.getMid());
            gradeMap.put(movieDTO.getMid(), String.format("%.1f", grade));
        });

        HttpSession session = httpServletRequest.getSession(false);
        String uid;
        if (!(session == null || session.getAttribute("uid") == null)) {
            model.addAttribute("uid", session.getAttribute("uid"));
            uid = session.getAttribute("uid").toString();
        }
        else
            uid = "";

        HashMap<String, Boolean> favMap = new HashMap<String, Boolean>();
        movieList.forEach(movieDTO -> {
            boolean isFav = favoriteService.isFavorite(uid, movieDTO.getMid());
            favMap.put(movieDTO.getMid(), isFav);
        });

        model.addAttribute("result", resultDTO);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("favMap", favMap);
        model.addAttribute("current", false);
        return "/page/movie_list";
    }

    @GetMapping("/movie/detail")
    public String movieDetail(String mid, PageRequestDTO reviewRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        MovieDTO movieDTO = movieService.read(mid);

        HttpSession session = httpServletRequest.getSession(false);
        String uid;
        if (!(session == null || session.getAttribute("uid") == null)) {
            model.addAttribute("uid", session.getAttribute("uid"));
            uid = session.getAttribute("uid").toString();
        }
        else
            uid = "";

        boolean isFav = favoriteService.isFavorite(uid, mid);

        model.addAttribute("dto", movieDTO);
        model.addAttribute("reviews", reviewService.getList(reviewRequestDTO, mid));
        model.addAttribute("my_review", reviewService.findMyReviewByMid(mid, uid));
        model.addAttribute("isFav", isFav);
        return "/page/movie_detail";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/booking";
    }

    @PostMapping("/booking/seat")
    public String seat(HttpServletRequest httpServletRequest, @RequestParam("mid") String mid, @RequestParam("date") String date, @RequestParam("time") String time, @RequestParam("tid") String tid, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return "redirect:/user/login";
        }
        System.out.println(mid);
        System.out.println(date);
        System.out.println(time);
        System.out.println(tid);

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
    public String pay(HttpServletRequest httpServletRequest, @RequestParam HashMap<String,String> params, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return "redirect:/user/login";
        }
        params.keySet().forEach(key -> {
            model.addAttribute(key, params.get(key));
        });
        return "page/pay";
    }

    @PostMapping("/booking/result")
    public String bookingResult(HttpServletRequest httpServletRequest, @RequestParam HashMap<String,String> params, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return "redirect:/user/login";
        }
        params.keySet().forEach(key -> {
            model.addAttribute(key, params.get(key));
            System.out.println(key + ": " +  params.get(key));
        });
        String uid = (String)session.getAttribute("uid");
        UserDTO userDTO = userService.getUserInfoById(uid);
        model.addAttribute("user", userDTO.getName());
        model.addAttribute("dooray_url", userDTO.getUrl());

        String randomId = reservationService.createReservationId();
        System.out.println(randomId);
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .rid(randomId)
                .tid(params.get("tid"))
                .uid((String) session.getAttribute("uid"))
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
                    .uid((String) session.getAttribute("uid"))
                    .build());
        }
        seatService.modify(dtoList, randomId);

        return "page/booking_result";
    }

    @GetMapping("/mypage/question/register")
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
        } else {

            UserDTO userDTO = userService.getUserInfoById((String) session.getAttribute("uid"));

            model.addAttribute("regDate", userDTO.getRegDate().toString().split("T")[0]);
            model.addAttribute("uid", userDTO.getUid());
            model.addAttribute("name", userDTO.getName());
            model.addAttribute("email", userDTO.getEmail());
            model.addAttribute("url", userDTO.getUrl());

            return "page/my_page_userinfo";
        }
    }

    @GetMapping("/mypage/ticket")
    public String my_page_ticket(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("result", reservationService.getMypageList(pageRequestDTO, (String) session.getAttribute("uid")));
            return "page/my_page_ticket";
        }
    }

    @GetMapping("/mypage/ticket/detail")
    public String my_page_ticket_detail(@RequestParam String rid, PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            List<String> result = seatService.getMySeatList(rid);
            String seatResult = "";

            for (int i = 0; i < result.size(); i++)
                seatResult += " " + result.get(i);

            model.addAttribute("seatResult", seatResult);
            model.addAttribute("result", reservationService.readReservation(rid));
            return "page/my_page_ticket_detail";
        }
    }

    @GetMapping("/mypage/ticket/delete")
    public String my_page_ticket_delete(@RequestParam String rid, PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .rid(rid)
                    .build();
            reservationService.delete(reservationDTO);
            return "redirect:/mypage/ticket";
        }
    }

    @GetMapping("/mypage/movie")
    public String my_page_mymovie(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            String uid = session.getAttribute("uid").toString();
            List<String> midList = favoriteService.getList(uid);
            PageResultDTO<MovieDTO, Movie> result = movieService.getListByMid(pageRequestDTO, midList);
            List<MovieDTO> movieList = result.getDtoList();

            HashMap<String, String> gradeMap = new HashMap<String, String>();
            movieList.forEach(movieDTO -> {
                float grade = reviewService.getGradeByMid(movieDTO.getMid());
                gradeMap.put(movieDTO.getMid(), String.format("%.1f", grade));
            });

            HashMap<String, Boolean> favMap = new HashMap<String, Boolean>();
            movieList.forEach(movieDTO -> {
                boolean isFav = favoriteService.isFavorite(uid, movieDTO.getMid());
                favMap.put(movieDTO.getMid(), isFav);
            });

            model.addAttribute("result", result);
            model.addAttribute("gradeMap", gradeMap);
            model.addAttribute("favMap", favMap);

            return "page/my_page_mymovie"; }
    }

    @GetMapping("/mypage/review")
    public String my_page_myreview(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            String uid = session.getAttribute("uid").toString();
            PageResultDTO<ReviewDTO, Review> resultDTO = reviewService.findMyReviews(pageRequestDTO, uid);
            List<ReviewDTO> reviewList = resultDTO.getDtoList();
            HashMap<String, String> titleMap = new HashMap<String, String>();
            reviewList.forEach(reviewDTO -> {
                String title = movieService.read(reviewDTO.getMid()).getName();
                titleMap.put(reviewDTO.getMid(), title);
            });

            model.addAttribute("uid", session.getAttribute("uid"));
            model.addAttribute("result", resultDTO);
            model.addAttribute("titleMap", titleMap);
            return "page/my_page_myreview";
        }
    }

    @GetMapping("/mypage/question")
    public String my_page_question(PageRequestDTO pageRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.getAttribute("uid") == null) {
            return "redirect:/user/login";
        } else {
            String uid = (String) session.getAttribute("uid");
            model.addAttribute("result", questionService.getQuestionList(pageRequestDTO, uid));
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
