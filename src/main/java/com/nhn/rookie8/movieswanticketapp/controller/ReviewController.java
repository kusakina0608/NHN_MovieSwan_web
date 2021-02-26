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
        if(reviewDTO.getUid().equals(""))
            return "redirect:/user/login";
        service.registerReview(reviewDTO);

        redirectAttributes.addAttribute("mid", reviewDTO.getMid());

        return "redirect:/movie/detail";
    }

    @PostMapping("/modify")
    public String modifyReview(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes) {
        log.info(reviewDTO);
        service.editReview(reviewDTO);

        redirectAttributes.addAttribute("mid", reviewDTO.getMid());
        return "redirect:/movie/detail";
    }

    @DeleteMapping("/delete")
    public String removeReview(@RequestParam("rid") String rid, @RequestParam("mid") String mid,
                               RedirectAttributes redirectAttributes) {
        service.deleteReview(rid);

        redirectAttributes.addAttribute("mid", mid);
        return "redirect:/movie/detail";
    }

    @ResponseBody
    @GetMapping("/getGrade")
    public float calculateGrade(String mid) {
        return service.getGradeByMid(mid);
    }
}
