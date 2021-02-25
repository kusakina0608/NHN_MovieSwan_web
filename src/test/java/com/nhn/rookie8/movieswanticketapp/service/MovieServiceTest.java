package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class MovieServiceTest {
    @Autowired
    private MovieService service;

    @MockBean
    private MovieRepository repository;

    @Test
    void readTest() {
        Movie movie = Movie.builder()
                .mid("AAAA001")
                .name("name")
                .poster("poster")
                .director("director")
                .actor("actor")
                .genre("genre")
                .runtime(100)
                .story("story")
                .startdate(LocalDate.of(2000, 1, 1))
                .enddate(LocalDate.of(2020, 12, 31))
                .build();

        doReturn(Optional.of(movie)).when(repository).findById("AAAA001");

        MovieDTO returnMovie = service.read("AAAA001");

        assertThat(returnMovie, is(service.entityToDTO(movie)));
    }

}
