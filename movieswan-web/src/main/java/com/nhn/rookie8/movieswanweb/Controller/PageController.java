package com.nhn.rookie8.movieswanweb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
    @GetMapping("/movie_list")
    public void movieList() {
    }
}
