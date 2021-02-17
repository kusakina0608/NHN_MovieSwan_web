package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ApiResultDTO;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/question")
@Log4j2
@RequiredArgsConstructor
@RestController
public class QuestionController {
    private final QuestionService service;

    @PostMapping("/register")
    public ApiResultDTO registerQuestion(QuestionDTO questionDTO) {
        log.info(questionDTO);
        return service.registerQuestion(questionDTO);
    }

    @GetMapping("/post")
    public QuestionDTO readQuestion(@RequestParam Integer qid) {
        return service.readQuestion(qid);
    }

    @GetMapping("/list")
    public void getQuestionList(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", service.getQuestionList(pageRequestDTO));
    }
}
