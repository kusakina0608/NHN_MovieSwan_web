package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/question")
@Controller

@RequiredArgsConstructor

@Log4j2
public class QuestionController {
    private final QuestionService service;

    @PostMapping("/register")
    public String registerQuestion(QuestionDTO questionDTO, Model model) {
        try {
            service.registerQuestion(questionDTO);
            return "redirect:/mypage/question";
        } catch (Exception e){
            log.error(e);
            return null;
        }
    }

    @GetMapping("/post")
    public QuestionDTO readQuestion(@RequestParam Integer qid) {
        try {
            return service.readQuestion(qid);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @GetMapping("/getall")
    public PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO pageRequestDTO, Model model) {
        try {
            return service.getAllQuestionList(pageRequestDTO);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
