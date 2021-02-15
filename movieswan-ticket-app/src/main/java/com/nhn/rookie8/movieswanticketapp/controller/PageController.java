package com.nhn.rookie8.movieswanticketapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Log4j2
@RequiredArgsConstructor
public class PageController {

    @GetMapping("/main")
    public String main_page() {
        return "page/main_page";
    }

    @GetMapping("/admin")
    public String admin_page() {
        return "page/admin_page";
    }
}
