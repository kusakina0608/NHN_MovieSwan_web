package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService service;

    @PostMapping("/register")
    public void registerReview(ReviewDTO reviewDTO) {
        service.register(reviewDTO);
    }

    @PutMapping("/modify")
    public void modifyReview(ReviewDTO reviewDTO) {
        service.modify(reviewDTO);
    }

    @DeleteMapping("/delete")
    public void removeReview(String rid) {
        service.remove(rid);
    }
}
