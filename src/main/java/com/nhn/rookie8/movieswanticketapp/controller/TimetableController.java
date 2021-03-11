package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.GetScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.service.TimetableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public GetScheduleDTO getAllSchedulesOfMovie(@RequestParam String movieId) {
        log.info("Request MovieId : {}", movieId);
        List<TimetableDTO> result = service.getAllTimetableOfMovie(movieId);

        List<LinkedHashMap<String, List<String>>> scheduleList = new ArrayList<>();
        LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<>();

        result.forEach(e -> {
            String[] datetime = e.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).split(" ");
            String date = datetime[0];
            String time = datetime[1];

            if (!linkedHashMap.containsKey(date)) {
                List<String> list = new ArrayList<>();
                linkedHashMap.put(date, list);
            }

            StringBuilder str = new StringBuilder();
            str.append(time).append(' ').append(e.getTimetableId());
            linkedHashMap.get(date).add(str.toString());
        });
        scheduleList.add(linkedHashMap);

        return GetScheduleDTO.builder()
                .scheduleData(scheduleList)
                .build();
    }
}
