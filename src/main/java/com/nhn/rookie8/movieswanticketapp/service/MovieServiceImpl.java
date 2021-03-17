package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.entity.QMovie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import com.nhn.rookie8.movieswanticketapp.repository.TimetableRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{
    private final MovieRepository movieRepository;
    private final TimetableRepository timetableRepository;

    @Override
    public String registerMovie(MovieDTO movieDTO) {
        String movieCode = movieDTO.getMovieId();

        Pageable pageable = PageRequest.of(0, 1000);
        BooleanBuilder booleanBuilder = moviesWithSameCodeBuilder(movieCode);

        long moviesWithSameCode = movieRepository.findAll(booleanBuilder, pageable).stream().count() + 1;
        StringBuilder movieId = new StringBuilder();
        movieId.append(movieCode).append(String.format("%03d", moviesWithSameCode));

        movieDTO.setMovieId(movieId.toString());
        Movie movie = dtoToEntity(movieDTO);
        movieRepository.save(movie);

        return movie.getMovieId();
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getMoviePage(PageRequestDTO requestDTO, boolean current) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startDate").descending());
        String keyword = requestDTO.getKeyword();
        BooleanBuilder booleanBuilder = current ? currentMoviesBuilder(keyword) : expectedMoviesBuilder(keyword);

        Page<Movie> result = movieRepository.findAll(booleanBuilder, pageable);

        return new PageResultDTO<>(result, this::entityToDTO);
    }

    @Override
    public PageResultDTO<MovieDTO, Movie> getListByMovieId(PageRequestDTO requestDTO, List<String> movieIdList) {
        Pageable pageable = requestDTO.getPageable(Sort.by("startDate").descending());
        BooleanBuilder booleanBuilder = moviesByMovieIdBuilder(movieIdList);

        Page<Movie> result = movieRepository.findAll(booleanBuilder, pageable);

        return new PageResultDTO<>(result, this::entityToDTO);
    }

    @Override
    public List<MovieDTO> getAllMovieList() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream().map(movie -> entityToDTO(movie)).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getCurrentMovieList() {
        BooleanBuilder booleanBuilder = currentMoviesBuilder(null);
        Pageable pageable = PageRequest.of(0, 1000);
        List<Movie> movieList = movieRepository.findAll(booleanBuilder, pageable).toList();
        return movieList.stream().map(movie -> entityToDTO(movie)).collect(Collectors.toList());
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

    @Override
    public MovieDTO entityToDTO(Movie entity) {
        Float rating = movieRepository.getAverageRating(entity.getMovieId());
        Integer timetableNum = movieRepository.getTimetableNum(entity.getMovieId(), LocalDateTime.now());

        return MovieDTO.builder()
                .movieId(entity.getMovieId())
                .title(entity.getTitle())
                .director(entity.getDirector())
                .actor(entity.getActor())
                .runtime(entity.getRuntime())
                .rating(rating == null ? 0 : rating)
                .reservationAvailable(timetableNum > 0)
                .genre(entity.getGenre())
                .story(entity.getStory())
                .poster(entity.getPoster())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    private BooleanBuilder moviesWithSameCodeBuilder(String movieId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        booleanBuilder.and(qMovie.movieId.startsWith(movieId));
        return booleanBuilder;
    }

    private BooleanBuilder currentMoviesBuilder(String keyword) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        booleanBuilder.and(qMovie.startDate.before(LocalDate.now()));
        booleanBuilder.and(qMovie.endDate.after(LocalDate.now()));
        if(keyword != null)
            booleanBuilder.and(qMovie.title.contains(keyword));
        return booleanBuilder;
    }

    private BooleanBuilder expectedMoviesBuilder(String keyword) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        booleanBuilder.and(qMovie.startDate.after(LocalDate.now()));
        if(keyword != null)
            booleanBuilder.and(qMovie.title.contains(keyword));
        return booleanBuilder;
    }

    private BooleanBuilder moviesByMovieIdBuilder(List<String> movieIdList) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMovie qMovie = QMovie.movie;
        if(movieIdList.isEmpty())
            booleanBuilder.and(qMovie.movieId.eq(""));
        for(String movieId : movieIdList)
            booleanBuilder.or(qMovie.movieId.eq(movieId));

        return booleanBuilder;
    }
}
