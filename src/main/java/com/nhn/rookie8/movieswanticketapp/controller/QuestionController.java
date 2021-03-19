package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.entity.Question;
import com.nhn.rookie8.movieswanticketapp.service.AnswerService;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/question")
@Controller

@RequiredArgsConstructor

@Log4j2
public class QuestionController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/register")
    public String registerQuestion(QuestionDTO questionDTO) {
        questionService.registerQuestion(questionDTO);
        return "redirect:/mypage/question";
    }

    @GetMapping("/post")
    public QuestionDTO readQuestion(@RequestParam Integer qid) {
        return questionService.readQuestion(qid);
    }

    @GetMapping("/getall")
    public PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO pageRequestDTO) {
        return questionService.getAllQuestionList(pageRequestDTO);
    }

    @PostMapping("/answer")
    public String registerAnswer(AnswerDTO answerDTO) {
        answerService.registerAnswer(answerDTO);
        return "redirect:/admin/question";
    }

    @ResponseBody
    @GetMapping("/isAnswered")
    public boolean isAnswered(@RequestParam Integer questionId) {
        return answerService.isAnswered(questionId);
    }

    @ResponseBody
    @GetMapping("/getAnswer")
    public AnswerDTO getAnswer(@RequestParam Integer questionId) {
        return answerService.readAnswer(questionId);
    }
}
