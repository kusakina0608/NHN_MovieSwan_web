package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;

import java.util.List;

public interface MovieService {
    String registerMovie(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Movie> getMoviePage(PageRequestDTO pageRequestDTO, boolean current);

    PageResultDTO<MovieDTO, Movie> getPageByMids(PageRequestDTO requestDTO, List<String> midList);

    List<MovieDTO> getAllMovieList();

    List<MovieDTO> getCurrentMovieList();

    MovieDTO getMovieDetail(String mid);

    default Movie dtoToEntity(MovieDTO movieDTO) {
        return Movie.builder()
                .movieId(movieDTO.getMovieId())
                .movieTitle(movieDTO.getMovieTitle())
                .poster(movieDTO.getPoster())
                .director(movieDTO.getDirector())
                .actor(movieDTO.getActor())
                .genre(movieDTO.getGenre())
                .runtime(movieDTO.getRuntime())
                .story(movieDTO.getStory())
                .startDate(movieDTO.getStartDate())
                .endDate(movieDTO.getEndDate())
                .build();
    }

    default MovieDTO entityToDTO(Movie movie) {
        return MovieDTO.builder()
                .movieId(movie.getMovieId())
                .movieTitle(movie.getMovieTitle())
                .poster(movie.getPoster())
                .director(movie.getDirector())
                .actor(movie.getActor())
                .genre(movie.getGenre())
                .runtime(movie.getRuntime())
                .story(movie.getStory())
                .startDate(movie.getStartDate())
                .endDate(movie.getEndDate())
                .build();
    }
}
