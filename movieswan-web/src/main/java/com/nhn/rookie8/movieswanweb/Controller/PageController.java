package com.nhn.rookie8.movieswanweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
public class PageController {
    @GetMapping("/list")
    public String movieList() {
        return "/page/movie_list";
    }
}
