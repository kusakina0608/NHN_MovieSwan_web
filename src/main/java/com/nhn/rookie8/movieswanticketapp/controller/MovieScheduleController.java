package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.GetScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInputDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
@RequestMapping("/api/schedule")
@RequiredArgsConstructor

@Log4j2
public class MovieScheduleController {
    private final MovieScheduleService service;

    @PostMapping("/register")
    public String registerMovieSchedule(MovieScheduleInputDTO movieScheduleInputDTO) {
        try {
            service.registerMovieSchedule(movieScheduleInputDTO);

            log.info("Request Input DTO : {}", movieScheduleInputDTO);

            return "redirect:/admin";
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @DeleteMapping("/delete")
    public String deleteMovieSchedule(String timetableId) {
        try {
            service.deleteMovieSchedule(timetableId);

            log.info("Request TimeTableID : {}", timetableId);

            return "redirect:/admin";
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @GetMapping("/get")
    @ResponseBody
    public MovieScheduleDTO getASchedule(@RequestParam String timetableId) {
        try {
            MovieScheduleDTO result = service.getASchedule(timetableId);

            log.info("Request TimeTableID : {}", timetableId);

            if (result == null)
                throw new Exception("No results were found for your search.");

            return service.getASchedule(timetableId);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @GetMapping("/getall")
    @ResponseBody
    public GetScheduleDTO getAllSchedulesOfMovie(@RequestParam String movieId) {
        try {
            log.info("Request MovieID : {}", movieId);
            List<MovieScheduleDTO> result = service.getAllSchedulesOfMovie(movieId);
            if (result.isEmpty())
                throw new Exception("No results were found for your search.");

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
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
