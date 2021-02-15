package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/schedule")
@Log4j2
@RequiredArgsConstructor
public class MovieScheduleController {
    private final MovieScheduleService service;

    @PostMapping("/register")
    public String register(MovieScheduleInpDTO inpdto) {

        service.registerSchedule(inpdto);
        return "redirect:/admin";
    }
}
