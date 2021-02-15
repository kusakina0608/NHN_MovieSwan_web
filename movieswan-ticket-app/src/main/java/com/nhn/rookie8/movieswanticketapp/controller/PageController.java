package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/movie")
@RequiredArgsConstructor
public class PageController {
    private final MovieService movieService;

    @GetMapping("/main")
    public String mainPage() {
        return "/layout/main_page";
    }

    @GetMapping("/list")
    public String movieList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", movieService.getList(pageRequestDTO));
        model.addAttribute("localDate", LocalDate.now());
        return "/page/movie_list";
    }

    @GetMapping("/booking")
    public String booking() {
        return "/page/booking";
    }
}
