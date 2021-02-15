package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInpDTO;
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
    public String register(MovieScheduleInpDTO inpdto) {
        service.registerMovieSchedule(inpdto);
        return "redirect:/admin";
    }

    @GetMapping("/test")
    public MovieScheduleResponse getSchedule() {
        String[] localDateTime = {LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))};

        log.info(localDateTime);
        List<Map<String, List<String>>> list = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();

        for (int i = 0; i < localDateTime.length; i++) {
            String[] datetime = localDateTime[i].split(" ");
            String date = datetime[0];
            String time = datetime[1];

            if (!map.containsKey(date)) {
                List<String> timelist =  new ArrayList<String>();
                map.put(date, timelist);
            }
            map.get(date).add(time);
        }
        list.add(map);

        MovieScheduleResponse movieScheduleResponse = MovieScheduleResponse.builder()
                .errorOccured(false)
                .data(list)
                .build();

        return movieScheduleResponse;
    }
}
