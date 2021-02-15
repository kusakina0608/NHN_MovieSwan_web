package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {
    private final MovieService service;

    @GetMapping("/main")
    public String mainPage() {
        return "/layout/main_page";
    }

    @GetMapping("/movie_list")
    public void movieList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/booking")
    public String booking() {
        return "/page/booking";
    }
}
