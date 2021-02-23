package com.nhn.rookie8.movieswanticketapp;

import com.nhn.rookie8.movieswanticketapp.service.MovieScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ScheduleServiceTests {
    @Autowired
    private MovieScheduleService service;

//    @Test
//    public void regTest() {
//        ScheduleInpDTO scheduleInpDTO = ScheduleInpDTO.builder()
//                .tid("0000002")
//                .mid("movie00001")
//                .time(LocalDateTime.now())
//                .build();
//
//        System.out.println(service.registerSchedule(scheduleInpDTO));
//    }
}
