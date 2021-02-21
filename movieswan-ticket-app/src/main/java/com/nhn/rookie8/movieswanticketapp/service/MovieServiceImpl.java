package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.entity.QMovie;
import com.nhn.rookie8.movieswanticketapp.entity.QReservation;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        String mid = movieDTO.getMid();

        Pageable pageable = PageRequest.of(0, (int)repository.count());
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMovie qMovie = QMovie.movie;
        BooleanExpression expression = qMovie.mid.startsWith(mid);
        booleanBuilder.and(expression);

        long moviesWithSameCode = repository.findAll(booleanBuilder, pageable).stream().count() + 1;
        mid += String.format("%03d", moviesWithSameCode);

        movieDTO.setMid(mid);

        Movie movie = dtoToEntity(movieDTO);

        repository.save(movie);

        return movie.getMid();
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getList(PageRequestDTO requestDTO, boolean current) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startdate").descending());
        BooleanBuilder booleanBuilder = current ? getReleaseMovies() : getExpectedMovies();
        QMovie qMovie = QMovie.movie;
        String keyword = requestDTO.getKeyword();

        if(keyword != null) {
            BooleanExpression expression = qMovie.name.contains(keyword);
            booleanBuilder.and(expression);
        }

        Page<Movie> result = repository.findAll(booleanBuilder, pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getListByMid(PageRequestDTO requestDTO, List<String> midList) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startdate").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        if(midList.isEmpty()) {
            BooleanExpression expression = qMovie.mid.eq("");
            booleanBuilder.and(expression);
        }
        for(String mid : midList) {
            BooleanExpression expression = qMovie.mid.eq(mid);
            booleanBuilder.or(expression);
        }

        Page<Movie> result = repository.findAll(booleanBuilder, pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<MovieDTO> getAllList() {
        List<Movie> movieList = repository.findAll();
        List<MovieDTO> movieDTOList = new ArrayList<MovieDTO>();

        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }

    @Override
    public List<MovieDTO> getReleaseList() {
        BooleanBuilder booleanBuilder = getReleaseMovies();
        Pageable pageable = PageRequest.of(0, (int)repository.count());
        List<Movie> movieList = repository.findAll(booleanBuilder, pageable).toList();
        List<MovieDTO> movieDTOList = new ArrayList<MovieDTO>();
        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }

    @Override
    public MovieDTO read(String mid) {
        Optional<Movie> result = repository.findById(mid);

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    private BooleanBuilder getReleaseMovies() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        BooleanExpression expression1 = qMovie.startdate.before(LocalDate.now());
        booleanBuilder.and(expression1);
        BooleanExpression expression2 = qMovie.enddate.after(LocalDate.now());
        booleanBuilder.and(expression2);
        return booleanBuilder;
    }

    private BooleanBuilder getExpectedMovies() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        BooleanExpression expression = qMovie.startdate.after(LocalDate.now());
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}
