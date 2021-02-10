package com.nhn.rookie8.movieswanweb.controller;

import com.nhn.rookie8.movieswanweb.dto.MovieDTO;
import com.nhn.rookie8.movieswanweb.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/booking")
@Log4j2
@RequiredArgsConstructor
public class BookingController {
    private final MovieService movieService;

    @GetMapping("/")
    public String booking(Model model){
        log.info("booking page......");
        List<MovieDTO> movieList = movieService.getReleaseMovieList();
        model.addAttribute("movieList", movieList);
        return "page/booking";
    }
}
