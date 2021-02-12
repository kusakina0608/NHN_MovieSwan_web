package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class PageController {
    private final MovieService movieService;

    @GetMapping("/booking")
    public String booking(Model model){
        log.info("booking");
        List<MovieDTO> movieList = movieService.getReleaseMovieList();
        model.addAttribute("movieList", movieList);
        return "page/booking";
    }

}
