package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieScheduleInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import com.nhn.rookie8.movieswanticketapp.repository.MovieScheduleRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"})
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class MovieScheduleServiceTest {
    @Mock
    MovieScheduleRepository movieScheduleRepository = mock(MovieScheduleRepository.class);

    @InjectMocks
    MovieScheduleService movieScheduleService = new MovieScheduleServiceImpl(movieScheduleRepository);

    private String mid;
    private LocalDate date;
    private LocalTime time;
    private LocalDateTime dateTime;
    private MovieSchedule testEntity;

    @BeforeAll
    public void beforeAllTest() {
        log.info("--- Before All Test ---");
        mid = "TEST000";
        dateTime = LocalDateTime.now();
        date = dateTime.toLocalDate();
        time = dateTime.toLocalTime();

        testEntity = movieScheduleService.dtoToEntity(MovieScheduleInputDTO.builder()
                .mid(new StringBuilder(mid).append('1').toString())
                .date(date.toString())
                .time(time.toString())
                .build());
    }

    @BeforeEach
    public void beforeEachTest() { log.info("--- Before Each Test ---"); }

    @Test
    @Order(0)
    public void registerScheduleTest() {
        for (int i = 0; i < 10; i++) {
            MovieScheduleInputDTO testDTO = MovieScheduleInputDTO.builder()
                    .mid(new StringBuilder(mid).append(i).toString())
                    .date(date.toString())
                    .time(time.plusMinutes(i).toString())
                    .build();

            StringBuilder builder = new StringBuilder();
            builder.append("aaa").append(dateTime.plusMinutes(i).format(DateTimeFormatter.ofPattern("yyMMddHHmm")));
            String tid = builder.toString();

            assertThat(tid, is(movieScheduleService.registerMovieSchedule(testDTO)));
        }
//        verify(movieScheduleRepository, times(10)).save(any());
    }

    @Test
    @Order(1)
    public void readScheduleTest() {
        MovieScheduleInputDTO testDTO = MovieScheduleInputDTO.builder()
                .mid(new StringBuilder(mid).append('1').toString())
                .date(date.toString())
                .time(time.toString())
                .build();

        StringBuilder builder = new StringBuilder();
        builder.append("aaa").append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));
        String tid = builder.toString();

        when(movieScheduleRepository.findById(tid)).thenReturn(Optional.of(movieScheduleService.dtoToEntity(testDTO)));
        MovieScheduleDTO result = movieScheduleService.getASchedule(tid);
        assertThat(tid, is(result.getTid()));
    }

    @Test
    @Order(2)
    public void readAllScheduleTest() {
        String testMid = "TEST0001";

        when(movieScheduleRepository.findByMidOrderByTimeAsc(mid)).thenReturn(Collections.emptyList());
        List<MovieScheduleDTO> movieScheduleList = movieScheduleService.getAllSchedulesOfMovie(testMid);

        assertThat(Collections.emptyList(), is(movieScheduleList));
    }

    @Test
    @Order(3)
    public void deleteScheduleTest() {
        StringBuilder builder = new StringBuilder();
        builder.append("aaa").append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));
        String tid = builder.toString();

        assertThat(tid, is(movieScheduleService.deleteMovieSchedule(tid)));
    }

    @AfterEach
    public void afterEachTest() { log.info("--- After Each Test ---"); }

    @AfterAll
    public void afterAllTest() { log.info("--- After All Test ---"); }
}