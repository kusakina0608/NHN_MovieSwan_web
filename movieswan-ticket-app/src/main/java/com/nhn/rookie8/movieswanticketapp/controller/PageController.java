package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PageController {
    private final MovieService movieService;

    @GetMapping("/main")
    public String mainPage() {
        return "page/main_page";
    }
  
    @GetMapping("/admin")
    public String admin_page() {
        return "page/admin_page";
    }

    @GetMapping("/movie/list")
    public void movieList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", movieService.getList(pageRequestDTO));
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
        // TODO: mid로 영화 조회해서 attribute로 넘겨주기
        model.addAttribute("title", "[15]극장판귀멸의칼날-무한열차편");
        model.addAttribute("theater", "무비스완 판교점");
        model.addAttribute("date", date);
        model.addAttribute("time", time);
        model.addAttribute("discount", "없음");
        return "page/seat";
    }

    @PostMapping("/booking/pay")
    public String pay(@RequestParam("seats") String seats) {
        System.out.println(seats);
        return "page/seat";
    }
}
