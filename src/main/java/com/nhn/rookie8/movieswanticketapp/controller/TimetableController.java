package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.service.TimetableService;
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
public class TimetableController {
    private final TimetableService service;

    @PostMapping("/register")
    public String registerMovieSchedule(TimetableInputDTO timetableInputDTO) {
        try {
            log.info("Request Input DTO : {}", timetableInputDTO);
            service.registerTimetable(timetableInputDTO);
            return "redirect:/admin";
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @DeleteMapping("/delete")
    public String deleteMovieSchedule(String tid) {
        try {
            log.info("Request TID : {}", tid);
            service.deleteTimetable(tid);
            return "redirect:/admin";
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @GetMapping("/get")
    @ResponseBody
    public TimetableDTO getASchedule(@RequestParam String tid) {
        try {
            log.info("Request TID : {}", tid);
            TimetableDTO result = service.getTimetable(tid);

            if (result == null)
                throw new Exception("No results were found for your search.");

            return service.getTimetable(tid);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @GetMapping("/getall")
    @ResponseBody
    // TODO: 컬렉션 반환값을 클래스로 처리하는 과정이 필요합니다. 추후 논의...
    public List<LinkedHashMap<String, List<String>>> getAllSchedulesOfMovie(@RequestParam String mid) {
        try {
            log.info("Request MID : {}", mid);
            List<TimetableDTO> result = service.getAllTimetable(mid);
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

            return scheduleList;
        } catch (Exception e) {
            log.error(e);
            return Collections.emptyList();
        }
    }
}
