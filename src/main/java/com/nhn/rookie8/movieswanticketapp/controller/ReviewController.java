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
    public String registerReview(@ModelAttribute ReviewDTO reviewDTO) {
        log.info(reviewDTO);

        if(reviewDTO.getMemberId().equals(""))
            return "redirect:/member/login";
        service.registerReview(reviewDTO);

        return "redirect:/movie/detail?movieId=" + reviewDTO.getMovieId();
    }

    @PostMapping("/modify")
    public String modifyReview(@ModelAttribute ReviewDTO reviewDTO) {
        log.info(reviewDTO);
        service.editReview(reviewDTO);
        return "redirect:/movie/detail?movieId=" + reviewDTO.getMovieId();
    }

    @DeleteMapping("/delete")
    public String removeReview(@RequestParam String reviewId, @RequestParam String movieId) {
        service.deleteReview(reviewId);
        return "redirect:/movie/detail?movieId=" + movieId;
    }

    @ResponseBody
    @GetMapping("/getRating")
    public double calculateRating(@RequestParam String movieId) {
        return service.getRatingByMovieId(movieId);
    }

    @ResponseBody
    @GetMapping("/getMyReview")
    public ReviewDTO getMyReview(@RequestParam String memberId, @RequestParam String movieId) {
        return service.findMyReviewByMovieId(movieId, memberId);
    }
}
