package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Log4j2
public class ReviewController {
    private final ReviewService service;

    @PostMapping("/register")
    public String registerReview(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes) {
        log.info(reviewDTO);
        if(reviewDTO.getMemberId().equals(""))
            return "redirect:/user/login";
        service.register(reviewDTO);

        redirectAttributes.addAttribute("movieId", reviewDTO.getMovieId());

        return "redirect:/movie/detail";
    }

    @PostMapping("/modify")
    public String modifyReview(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes) {
        log.info(reviewDTO);
        service.modify(reviewDTO);

        redirectAttributes.addAttribute("movieId", reviewDTO.getMovieId());
        return "redirect:/movie/detail";
    }

    @DeleteMapping("/delete")
    public String removeReview(@RequestParam("rid") String rid, @RequestParam("movieId") String movieId,
                               RedirectAttributes redirectAttributes) {
        service.remove(rid);

        redirectAttributes.addAttribute("movieId", movieId);
        return "redirect:/movie/detail";
    }

    @ResponseBody
    @GetMapping("/getRating")
    public float calculateRating(String movieId) {
        return service.getRatingByMovieId(movieId);
    }
}
