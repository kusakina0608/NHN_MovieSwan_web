package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/question")
@Log4j2
@RequiredArgsConstructor
@RestController
public class QuestionController {
    private final QuestionService service;

    @PostMapping("/register")
    public String registerQuestion(QuestionDTO questionDTO) {
        service.registerQuestion(questionDTO);
        return "redirect:/question/list";
    }

    @GetMapping("/post")
    public QuestionDTO readQuestion(@RequestParam Integer qid) {
        return service.readQuestion(qid);
    }
}
