package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.entity.QMovie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
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

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{
    private final MovieRepository repository;

    @Override
    public String register(MovieDTO movieDTO) {
        String mid = movieDTO.getMovieId();

        Pageable pageable = PageRequest.of(0, 1000);
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMovie qMovie = QMovie.movie;
        BooleanExpression expression = qMovie.movieId.startsWith(mid);
        booleanBuilder.and(expression);

        long moviesWithSameCode = repository.findAll(booleanBuilder, pageable).stream().count() + 1;
        mid += String.format("%03d", moviesWithSameCode);

        movieDTO.setMovieId(mid);

        Movie movie = dtoToEntity(movieDTO);

        repository.save(movie);

        return movie.getMovieId();
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getList(PageRequestDTO requestDTO, boolean current) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startDate").descending());
        BooleanBuilder booleanBuilder = current ? getReleaseMovies() : getExpectedMovies();
        QMovie qMovie = QMovie.movie;
        String keyword = requestDTO.getKeyword();

        if(keyword != null) {
            BooleanExpression expression = qMovie.title.contains(keyword);
            booleanBuilder.and(expression);
        }

        Page<Movie> result = repository.findAll(booleanBuilder, pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getListByMid(PageRequestDTO requestDTO, List<String> midList) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startDate").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        if(midList.isEmpty()) {
            BooleanExpression expression = qMovie.movieId.eq("");
            booleanBuilder.and(expression);
        }
        for(String mid : midList) {
            BooleanExpression expression = qMovie.movieId.eq(mid);
            booleanBuilder.or(expression);
        }

        Page<Movie> result = repository.findAll(booleanBuilder, pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<MovieDTO> getAllList() {
        List<Movie> movieList = repository.findAll();
        List<MovieDTO> movieDTOList = new ArrayList<>();

        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }

    @Override
    public List<MovieDTO> getReleaseList() {
        BooleanBuilder booleanBuilder = getReleaseMovies();
        Pageable pageable = PageRequest.of(0, 1000);
        List<Movie> movieList = repository.findAll(booleanBuilder, pageable).toList();
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }

    @Override
    public MovieDTO getMovie(String mid) {
        Optional<Movie> result = repository.findById(mid);

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    private BooleanBuilder getReleaseMovies() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        BooleanExpression expression1 = qMovie.startDate.before(LocalDate.now());
        booleanBuilder.and(expression1);
        BooleanExpression expression2 = qMovie.endDate.after(LocalDate.now());
        booleanBuilder.and(expression2);
        return booleanBuilder;
    }

    private BooleanBuilder getExpectedMovies() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        BooleanExpression expression = qMovie.startDate.after(LocalDate.now());
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}
