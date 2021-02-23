package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.ApiResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
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
@Log4j2
@RequiredArgsConstructor
@Controller
public class QuestionController {
    private final QuestionService service;

    @PostMapping("/register")
    public String registerQuestion(QuestionDTO questionDTO, Model model) {
        ApiResultDTO response = service.registerQuestion(questionDTO);
        if (response.getError()) {
            model.addAttribute("dto", questionDTO);
            return "redirect:/question";
        } else {
            model.addAttribute("status", "success");
            return "redirect:/mypage/question";
        }
    }

    @GetMapping("/post")
    public QuestionDTO readQuestion(@RequestParam Integer qid) {
        return service.readQuestion(qid);
    }

    @GetMapping("/list")
    public void getQuestionListAdmin(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result", service.getQuestionListAdmin(pageRequestDTO));
    }
}
