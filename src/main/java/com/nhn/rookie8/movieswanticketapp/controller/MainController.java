package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final MovieService movieService;


    @Value("${accountURL}")
    private String accountUrl;

    @GetMapping({"", "/", "/main"})
    public String mainPage() {
        return "page/main_page";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<MovieDTO> movieList = movieService.getCurrentMovieList();
        model.addAttribute("movieList", movieList);
        return "page/admin_page";
    }

}
