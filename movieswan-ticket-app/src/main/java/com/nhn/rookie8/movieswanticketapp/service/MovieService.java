package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;

import java.util.List;

public interface MovieService {
    //String register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Movie> getList(PageRequestDTO pageRequestDTO);

    List<MovieDTO> getAllList();

    List<MovieDTO> getReleaseList();

    //MovieDTO read(String mid);

    //void modify(MovieDTO movieDTO);

    default MovieDTO entityToDTO(Movie movie) {
        MovieDTO movieDTO = MovieDTO.builder()
                .mid(movie.getMid())
                .name(movie.getName())
                .poster(movie.getPoster())
                .director(movie.getDirector())
                .actor(movie.getActor())
                .genre(movie.getGenre())
                .runtime(movie.getRuntime())
                .story(movie.getStory())
                .startdate(movie.getStartdate())
                .enddate(movie.getEnddate())
                .build();

        return movieDTO;
    }
    default Movie dtoToEntity(MovieDTO movieDTO) {
        Movie movie = Movie.builder()
                .mid(movieDTO.getMid())
                .name(movieDTO.getName())
                .poster(movieDTO.getPoster())
                .director(movieDTO.getDirector())
                .actor(movieDTO.getActor())
                .genre(movieDTO.getGenre())
                .runtime(movieDTO.getRuntime())
                .story(movieDTO.getStory())
                .startdate(movieDTO.getStartdate())
                .enddate(movieDTO.getEnddate())
                .build();

        return movie;
    }
}
