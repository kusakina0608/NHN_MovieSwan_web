package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.entity.QMovie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import com.nhn.rookie8.movieswanticketapp.repository.TimetableRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{
    private final MovieRepository movieRepository;
    private final TimetableRepository timetableRepository;

    @Override
    public String registerMovie(MovieDTO movieDTO) {
        String movieId = movieDTO.getMovieId();

        Pageable pageable = PageRequest.of(0, 1000);
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMovie qMovie = QMovie.movie;
        BooleanExpression expression = qMovie.movieId.startsWith(movieId);
        booleanBuilder.and(expression);

        long moviesWithSameCode = movieRepository.findAll(booleanBuilder, pageable).stream().count() + 1;
        movieId += String.format("%03d", moviesWithSameCode);

        movieDTO.setMovieId(movieId);

        Movie movie = dtoToEntity(movieDTO);

        movieRepository.save(movie);

        return movie.getMovieId();
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getMoviePage(PageRequestDTO requestDTO, boolean current) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startDate").descending());
        BooleanBuilder booleanBuilder = current ? currentMoviesBuilder() : expectedMoviesBuilder();
        QMovie qMovie = QMovie.movie;
        String keyword = requestDTO.getKeyword();

        if(keyword != null) {
            BooleanExpression expression = qMovie.title.contains(keyword);
            booleanBuilder.and(expression);
        }

        Page<Movie> result = movieRepository.findAll(booleanBuilder, pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getListByMovieId(PageRequestDTO requestDTO, List<String> movieIdList) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startDate").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        if(movieIdList.isEmpty()) {
            BooleanExpression expression = qMovie.movieId.eq("");
            booleanBuilder.and(expression);
        }
        for(String movieId : movieIdList) {
            BooleanExpression expression = qMovie.movieId.eq(movieId);
            booleanBuilder.or(expression);
        }

        Page<Movie> result = movieRepository.findAll(booleanBuilder, pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<MovieDTO> getAllMovieList() {
        List<Movie> movieList = movieRepository.findAll();
        List<MovieDTO> movieDTOList = new ArrayList<>();

        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }

    @Override
    public List<MovieDTO> getCurrentMovieList() {
        BooleanBuilder booleanBuilder = currentMoviesBuilder();
        Pageable pageable = PageRequest.of(0, 1000);
        List<Movie> movieList = movieRepository.findAll(booleanBuilder, pageable).toList();
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }

    @Override
    public List<MovieDTO> getScheduledMovieList() {
        return getCurrentMovieList()
                .stream()
                .filter(e -> timetableRepository.existsByMovieId(e.getMovieId()))
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getMovieDetail(String movieId) {
        Optional<Movie> result = movieRepository.findById(movieId);

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    private BooleanBuilder currentMoviesBuilder() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        BooleanExpression expression1 = qMovie.startDate.before(LocalDate.now());
        booleanBuilder.and(expression1);
        BooleanExpression expression2 = qMovie.endDate.after(LocalDate.now());
        booleanBuilder.and(expression2);
        return booleanBuilder;
    }

    private BooleanBuilder expectedMoviesBuilder() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        BooleanExpression expression = qMovie.startDate.after(LocalDate.now());
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}
