package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import com.querydsl.core.BooleanBuilder;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @BeforeAll
    void createMovieDto() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(4, 4);
        EasyRandom generator = new EasyRandom();

        movieDTO = generator.nextObject(MovieDTO.class);

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
        String mid = movie.getMid();
        when(repository.findById(mid)).thenReturn(Optional.of(movie));

        MovieDTO returnMovie = service.read(mid);

        assertThat(returnMovie, is(service.entityToDTO(movie)));
    }

    @Test
    void movieListTest() {
        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(4, 4);
        EasyRandom generator = new EasyRandom();
        List<MovieDTO> movieDTOList = generator.objects(MovieDTO.class, 10).collect(Collectors.toList());
        List<Movie> movieList = movieDTOList.stream().map(dto -> service.dtoToEntity(dto)).collect(Collectors.toList());

        when(repository.findAll()).thenReturn(movieList);

        List<MovieDTO> returnList = service.getAllList();

        assertThat(returnList, is(movieDTOList));
    }
}
