package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.ScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/schedule")
@Log4j2
@RequiredArgsConstructor
public class APIController {
    private final ScheduleService service;

    @PostMapping("/register")
    public String register(ScheduleDTO dto, RedirectAttributes redirectAttributes) {
        log.info("dto..." + dto);
        log.info("-------------------------------------------");

        String tid = service.registerSchedule(dto);
        redirectAttributes.addFlashAttribute("msg", tid);
        return "redirect:/admin";
    }

    @GetMapping("/")
}
