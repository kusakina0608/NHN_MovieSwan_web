package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
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
import org.springframework.data.domain.*;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieServiceTest {
    @Autowired
    private MovieService service;

    @MockBean
    private MovieRepository repository;

    EasyRandomParameters parameters;
    EasyRandom generator;

    private MovieDTO movieDTO;
    private Movie movie;

    @BeforeAll
    void createMovieDto() {
        parameters = new EasyRandomParameters();
        parameters.stringLengthRange(4, 4);
        generator = new EasyRandom(parameters);

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

        String movieId = service.registerMovie(service.entityToDTO(movie));

        assertThat(movieId, is(movie.getMovieId() + "001"));
    }

    @Test
    void movieReadTest() {
        String movieId = movie.getMovieId();
        when(repository.findById(movieId)).thenReturn(Optional.of(movie));

        MovieDTO returnMovie = service.getMovieDetail(movieId);

        assertThat(returnMovie, is(service.entityToDTO(movie)));
    }

    @Test
    void movieListTest() {
        List<MovieDTO> movieDTOList = generator.objects(MovieDTO.class, 10).collect(Collectors.toList());
        List<Movie> movieList = movieDTOList.stream().map(dto -> service.dtoToEntity(dto)).collect(Collectors.toList());

        when(repository.findAll()).thenReturn(movieList);

        List<MovieDTO> returnList = service.getAllMovieList();

        assertThat(returnList, is(movieDTOList));
    }

    @Test
    void movieReleaseListTest() {
        List<MovieDTO> movieDTOList = generator.objects(MovieDTO.class, 10).collect(Collectors.toList());
        List<Movie> movieList = movieDTOList.stream().map(dto -> service.dtoToEntity(dto)).collect(Collectors.toList());
        Page<Movie> moviePage = new PageImpl(movieList);

        when(repository.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(moviePage);

        List<MovieDTO> returnList = service.getCurrentMovieList();

        assertThat(returnList, is(movieDTOList));
    }

    @Test
    void moviePageTest() {
        List<MovieDTO> movieDTOList = generator.objects(MovieDTO.class, 10).collect(Collectors.toList());
        List<Movie> movieList = movieDTOList.stream().map(dto -> service.dtoToEntity(dto)).collect(Collectors.toList());
        Page<Movie> moviePage = mock(Page.class);

        when(moviePage.getPageable()).thenReturn(PageRequest.of(0, 10));
        when(moviePage.stream()).thenReturn(movieList.stream());
        PageRequestDTO requestDTO = PageRequestDTO.builder().page(1).size(10).build();

        when(repository.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(moviePage);

        PageResultDTO<MovieDTO, Movie> resultDTO = service.getMoviePage(requestDTO, false);
        List<MovieDTO> returnDTOList = resultDTO.getDtoList();

        assertThat(returnDTOList, is(movieDTOList));
    }

    @Test
    void moviePageByMovieIdTest() {
        List<MovieDTO> movieDTOList = generator.objects(MovieDTO.class, 10).collect(Collectors.toList());
        List<Movie> movieList = movieDTOList.stream().map(dto -> service.dtoToEntity(dto)).collect(Collectors.toList());
        Page<Movie> moviePage = mock(Page.class);
        List<String> movieIdList = movieList.stream().map(movie -> movie.getMovieId()).collect(Collectors.toList());

        when(moviePage.getPageable()).thenReturn(PageRequest.of(0, 10));
        when(moviePage.stream()).thenReturn(movieList.stream());
        PageRequestDTO requestDTO = PageRequestDTO.builder().page(1).size(10).build();

        when(repository.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(moviePage);

        PageResultDTO<MovieDTO, Movie> resultDTO = service.getListByMovieId(requestDTO, movieIdList);
        List<MovieDTO> returnDTOList = resultDTO.getDtoList();

        assertThat(returnDTOList, is(movieDTOList));
    }
}
