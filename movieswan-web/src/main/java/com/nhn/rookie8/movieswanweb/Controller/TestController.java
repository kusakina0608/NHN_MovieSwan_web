package com.nhn.rookie8.movieswanweb.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layout")
@Log4j2
public class TestController {
    @GetMapping("/common_page")
    public void commonPage() {
    }

    @GetMapping("/main_page")
    public void mainPage() {
    }
}
