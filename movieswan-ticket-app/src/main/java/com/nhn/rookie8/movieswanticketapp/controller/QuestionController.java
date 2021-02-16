package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/question")
@Log4j2
@RequiredArgsConstructor
@RestController
public class QuestionController {
    private final QuestionService service;

    @PostMapping("/register")
    public void registerQuest(QuestionDTO questionDTO) {
        service.registerQuestion(questionDTO);
    }
}
