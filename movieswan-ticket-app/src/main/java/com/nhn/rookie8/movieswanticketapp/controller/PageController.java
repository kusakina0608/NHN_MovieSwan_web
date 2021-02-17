package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PageController {
    private final MovieService movieService;
    private final QuestionService questionService;

    @GetMapping({"/", "/main"})
    public String main_page() {
        return "page/main_page";
    }

    @GetMapping("/admin")
    public String admin_page(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/admin_page";
    }

    @GetMapping("/movie/current/list")
    public String currentMovieList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", movieService.getList(pageRequestDTO, true));
        model.addAttribute("current", true);
        return "/page/movie_list";
    }

    @GetMapping("/movie/expected/list")
    public String expectedMovieList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", movieService.getList(pageRequestDTO, false));
        model.addAttribute("current", false);
        return "/page/movie_list";
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/booking";
    }

    @PostMapping("/booking/seat")
    public String seat(@RequestParam("mid") String mid, @RequestParam("date") String date, @RequestParam("time") String time, Model model) {
        System.out.println(mid);
        System.out.println(date);
        System.out.println(time);
        // TODO: mid로 영화 조회
//        MovieDTO movieDTO = movieService.getMovie(mid);
        model.addAttribute("title", "[15]극장판귀멸의칼날-무한열차편");
//        model.addAttribute("title", movieDTO.getName());
        model.addAttribute("poster", "/asset/image/poster99.jpg");
//        model.addAttribute("poster", movieDTO.getPoster());
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        if(Integer.parseInt(time.split(":")[0]) < 9){
            model.addAttribute("discount", "조조 할인(오전 09:00 이전)");
        }
        else{
            model.addAttribute("discount", "없음");
        }
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
        });
        return "page/booking_result";
    }

    @GetMapping("/question")
    public String question_page() {
        return "page/question_page";
    }

    // 여기부터 전부 마이페이지 입니다...
    @GetMapping("/mypage")
    public String my_page() {
        return "redirect:/mypage/userinfo";
    }

    @GetMapping("/mypage/question")
    public String my_page_question(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", questionService.getQuestionList(pageRequestDTO));
        return "page/my_page_question";
    }

    @GetMapping("/mypage/question/post")
    public String my_page_read_question(@RequestParam("qid") Integer qid, Model model) {
        QuestionDTO dto = questionService.readQuestion(qid);
        model.addAttribute("dto", dto);
        return "page/my_page_read_question";
    }
