package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.ScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/schedule")
@RequiredArgsConstructor

@Log4j2
public class TimetableController {
    private final TimetableService service;

    @PostMapping("/register")
    public String registerMovieSchedule(TimetableInputDTO timetableInputDTO) {
        service.registerTimetable(timetableInputDTO);
        log.info("Request Input DTO : {}", timetableInputDTO);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete")
    public String deleteMovieSchedule(String timetableId) {
        service.deleteTimetable(timetableId);

        log.info("Request TimeTableID : {}", timetableId);

        return "redirect:/admin";
    }

    @GetMapping("/get")
    @ResponseBody
    public TimetableDTO getSchedule(@RequestParam String timetableId) {
        TimetableDTO result = service.getTimetable(timetableId);

        log.info("Request TimeTableID : {}", timetableId);

        return service.getTimetable(timetableId);
    }

    @GetMapping("/getall")
    @ResponseBody
    public List<ScheduleDTO> getAllMovieSchedules(@RequestParam String movieId) {
        log.info("Request MovieId : {}", movieId);
        return service.getMovieScheduleDetail(service.getAllTimetableOfMovie(movieId));

    }
}
