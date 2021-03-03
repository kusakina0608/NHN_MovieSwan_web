package com.nhn.rookie8.movieswanticketapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final MovieService movieService;


    @Value("${accountURL}")
    private String accountUrl;

    @GetMapping({"", "/", "/main"})
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

}
