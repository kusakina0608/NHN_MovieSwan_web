package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import com.nhn.rookie8.movieswanticketapp.response.MovieScheduleResponse;
import com.nhn.rookie8.movieswanticketapp.service.MovieScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/schedule")
@Log4j2
@RequiredArgsConstructor
@RestController
public class MovieScheduleController {
    private final MovieScheduleService service;

    @PostMapping("/register")
    public void registerSchd(MovieScheduleInpDTO inpDTO) {
        service.registerMovieSchedule(inpDTO);
    }

    @DeleteMapping("/delete")
    public void deleteSchd(String tid) {
        service.deleteMovieSchedule(tid);
    }

    @GetMapping("/timetable")
    public MovieScheduleResponse findSchd(@RequestParam String mid) {
        List<MovieScheduleDTO> scheduleDTOList = service.getMovieSchedule(mid);
        List<Map<String, List<String>>> scheduleList = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();

        scheduleDTOList.forEach(e -> {
            String[] datetime = e.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).split(" ");
            String date = datetime[0];
            String time = datetime[1];

            if (!map.containsKey(date)) {
                List<String> list = new ArrayList<String>();
                map.put(date, list);
            }
            map.get(date).add(time);
        });
        scheduleList.add(map);

        MovieScheduleResponse movieScheduleResponse = MovieScheduleResponse.builder()
                .errorOccured(false)
                .data(scheduleList)
                .build();

        return movieScheduleResponse;
    }

    @GetMapping("/get")
    public List<MovieScheduleDTO> getSchd(@RequestParam String mid) {
        List<MovieScheduleDTO> scheduleDTOList = service.getMovieSchedule(mid);
        return service.getMovieSchedule(mid);
    }
}