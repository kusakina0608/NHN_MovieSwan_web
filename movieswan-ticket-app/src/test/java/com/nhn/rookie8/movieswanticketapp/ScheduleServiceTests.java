package com.nhn.rookie8.movieswanticketapp;

import com.nhn.rookie8.movieswanticketapp.dto.ScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
public class ScheduleServiceTests {
    @Autowired
    private ScheduleService service;

    @Test
    public void regTest() {
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .tid("00001")
                .mid("movie001")
                .time(LocalDateTime.now())
                .build();

        System.out.println(service.registerSchedule(scheduleDTO));
    }
}
