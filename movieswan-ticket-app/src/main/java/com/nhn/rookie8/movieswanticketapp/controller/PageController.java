package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PageController {
    private final MovieService movieService;

    @GetMapping("/main")
    public String mainPage() {
        return "/layout/main_page";
    }

    @GetMapping("/movie_list")
    public void movieList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    @GetMapping("/booking")
    public String booking(Model model) {
        List<MovieDTO> movieList = movieService.getReleaseList();
        model.addAttribute("movieList", movieList);
        return "page/booking";
    }
}
