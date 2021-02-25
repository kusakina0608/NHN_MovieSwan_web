package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieServiceTest {
    @Autowired
    private MovieService service;

    @MockBean
    private MovieRepository repository;

    private MovieDTO movieDTO;
    private Movie movie;
    private String mid;
    private String name;
    private String poster;
    private String director;
    private String actor;
    private String genre;
    private int runtime;
    private String story;
    private LocalDate startdate;
    private LocalDate enddate;

    @BeforeAll
    void createMovieDto() {
        mid = "AAAA";
        name = "name";
        poster = "poster";
        director = "director";
        actor = "actor";
        genre = "genre";
        runtime = 100;
        story = "story";
        startdate = LocalDate.of(2000, 1, 1);
        enddate = LocalDate.of(2020, 12, 31);

        movieDTO = MovieDTO.builder()
                .mid(mid)
                .name(name)
                .poster(poster)
                .director(director)
                .actor(actor)
                .genre(genre)
                .runtime(runtime)
                .story(story)
                .startdate(startdate)
                .enddate(enddate)
                .build();
        movie = service.dtoToEntity(movieDTO);
    }

    @Test
    void movieRegisterTest() {
        List<Movie> movieList = new ArrayList<>();
        Page<Movie> page = new PageImpl(movieList);

        when(repository.save(movie)).thenReturn(movie);
        when(repository.findAll(any(BooleanBuilder.class), any(Pageable.class)))
                .thenReturn(page);

        String mid = service.register(service.entityToDTO(movie));

        assertThat(mid, is(movie.getMid() + "001"));
    }

    @Test
    void movieReadTest() {
        when(repository.findById("AAAA001")).thenReturn(Optional.of(movie));

        MovieDTO returnMovie = service.read("AAAA001");

        assertThat(returnMovie, is(service.entityToDTO(movie)));
    }

}
