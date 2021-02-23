package com.nhn.rookie8.movieswanticketapp.controller;

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
@ResponseBody

@Log4j2
public class MovieScheduleController {
    private final MovieScheduleService service;

    @PostMapping("/register")
    public void registerMovieSchedule(MovieScheduleInputDTO movieScheduleInputDTO) {
        service.registerMovieSchedule(movieScheduleInputDTO);
    }

    @DeleteMapping("/delete")
    public void deleteMovieSchedule(String tid) {
        service.deleteMovieSchedule(tid);
    }

    @GetMapping("/get")
    public MovieScheduleDTO getASchedule(@RequestParam String tid) {
        return service.getASchedule(tid);
    }

    @GetMapping("/getall")
    // TODO: 컬렉션 반환값을 클래스로 처리하는 과정이 필요합니다. 추후 논의...
    public List<LinkedHashMap<String, List<String>>> getAllSchedulesOfMovie(@RequestParam String mid) {
        List<MovieScheduleDTO> result = service.getAllSchedulesOfMovie(mid);

        List<LinkedHashMap<String, List<String>>> scheduleList = new ArrayList<>();
        LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<>();

        result.forEach(e -> {
            String[] datetime = e.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).split(" ");
            String date = datetime[0];
            String time = datetime[1];

            if (!linkedHashMap.containsKey(date)) {
                List<String> list = new ArrayList<>();
                linkedHashMap.put(date, list);
            }

            StringBuilder str = new StringBuilder();
            str.append(time).append(' ').append(e.getTid());
            linkedHashMap.get(date).add(str.toString());
        });
        scheduleList.add(linkedHashMap);

        return scheduleList;
    }

    @GetMapping("/getlist")
    public List<MovieScheduleDTO> getAllSchedulesListOfMovie(@RequestParam String mid) {
        List<MovieScheduleDTO> scheduleDTOList = service.getAllSchedulesOfMovie(mid);
        return service.getAllSchedulesOfMovie(mid);
    }




}
