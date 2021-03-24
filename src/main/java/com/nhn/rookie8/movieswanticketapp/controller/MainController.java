package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final MovieService movieService;

    @Value("${accountURL}")
    private String accountUrl;

    @GetMapping({"", "/", "/main"})
    public String mainPage(@RequestParam(required = false) String id, @RequestParam(required = false) String name, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        return "page/main_page";
    }
}
