package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import com.nhn.rookie8.movieswanticketapp.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@Log4j2
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final MovieService movieService;
    private final QuestionService questionService;

    @GetMapping("/")
    public String adminPage(Model model) {
        model.addAttribute("movieList", movieService.getCurrentMovieList());
        return "page/admin_page";
    }

    @GetMapping("/question")
    public String adminQuestionPage(PageRequestDTO pageRequestDTO, Model model) {
        model.addAttribute("result",
                questionService.getAllQuestionList(pageRequestDTO));
        return "page/admin_question_page";
    }

    @GetMapping("/answer")
    public String adminAnswerPage(@RequestParam Integer questionId, Model model) {
        model.addAttribute("dto", questionService.readQuestion(questionId));
        return "page/admin_answer_page";
    }
}
