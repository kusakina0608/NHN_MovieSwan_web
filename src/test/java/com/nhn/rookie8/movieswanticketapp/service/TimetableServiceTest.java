package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Timetable;
import com.nhn.rookie8.movieswanticketapp.repository.TimetableRepository;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"})
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class TimetableServiceTest {
    @Mock
    TimetableRepository timetableRepository = mock(TimetableRepository.class);

    @InjectMocks
    TimetableService timetableService = new TimetableServiceImpl(timetableRepository);

    private String movieId;
    private LocalDate date;
    private LocalTime time;
    private LocalDateTime dateTime;
    private Timetable testEntity;

    @BeforeAll
    public void beforeAllTest() {
        log.info("--- Before All Test ---");
        movieId = "TEST000";
        dateTime = LocalDateTime.now();
        date = dateTime.toLocalDate();
        time = dateTime.toLocalTime();

        testEntity = timetableService.dtoToEntity(TimetableInputDTO.builder()
                .movieId(new StringBuilder(movieId).append('1').toString())
                .startDate(date.toString())
                .startTime(time.toString())
                .build());
    }

    @BeforeEach
    public void beforeEachTest() { log.info("--- Before Each Test ---"); }

    @Test
    @Order(0)
    public void registerScheduleTest() {
        for (int i = 0; i < 10; i++) {
            TimetableInputDTO testDTO = TimetableInputDTO.builder()
                    .movieId(new StringBuilder(movieId).append(i).toString())
                    .startDate(date.toString())
                    .startTime(time.plusMinutes(i).toString())
                    .build();

            StringBuilder builder = new StringBuilder();
            builder.append("aaa").append(dateTime.plusMinutes(i).format(DateTimeFormatter.ofPattern("yyMMddHHmm")));
            String timetableId = builder.toString();

            assertThat(timetableId, is(timetableService.registerTimetable(testDTO)));
        }
//        verify(movieScheduleRepository, times(10)).save(any());
    }

    @Test
    @Order(1)
    public void readScheduleTest() {
        TimetableInputDTO testDTO = TimetableInputDTO.builder()
                .movieId(new StringBuilder(movieId).append('1').toString())
                .startDate(date.toString())
                .startTime(time.toString())
                .build();

        StringBuilder builder = new StringBuilder();
        builder.append("aaa").append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));
        String timetableId = builder.toString();

        when(timetableRepository.findById(timetableId)).thenReturn(Optional.of(timetableService.dtoToEntity(testDTO)));
        TimetableDTO result = timetableService.getTimetable(timetableId);
        assertThat(timetableId, is(result.getTimetableId()));
    }

    @Test
    @Order(2)
    public void readAllScheduleTest() {
        String testMovieId = "TEST0001";

        when(timetableRepository.findByMovieIdOrderByStartTimeAsc(movieId)).thenReturn(Collections.emptyList());
        List<TimetableDTO> movieScheduleList = timetableService.getAllTimetableOfMovie(testMovieId);

        assertThat(Collections.emptyList(), is(movieScheduleList));
    }

    @Test
    @Order(3)
    public void deleteScheduleTest() {
        StringBuilder builder = new StringBuilder();
        builder.append("aaa").append(dateTime.format(DateTimeFormatter.ofPattern("yyMMddHHmm")));
        String timetableId = builder.toString();

        assertThat(timetableId, is(timetableService.deleteTimetable(timetableId)));
    }

    @AfterEach
    public void afterEachTest() { log.info("--- After Each Test ---"); }

    @AfterAll
    public void afterAllTest() { log.info("--- After All Test ---"); }
}