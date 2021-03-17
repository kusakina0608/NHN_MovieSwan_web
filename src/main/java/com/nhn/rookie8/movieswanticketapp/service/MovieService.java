package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;

import java.util.List;

public interface MovieService {
    String registerMovie(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Movie> getMoviePage(PageRequestDTO pageRequestDTO, boolean current);

    PageResultDTO<MovieDTO, Movie> getListByMovieId(PageRequestDTO requestDTO, List<String> movieIdList);

    List<MovieDTO> getAllMovieList();

    List<MovieDTO> getCurrentMovieList();

    List<MovieDTO> getScheduledMovieList();

    MovieDTO getMovieDetail(String movieId);

    MovieDTO entityToDTO(Movie movie);

    default Movie dtoToEntity(MovieDTO movieDTO) {
        return Movie.builder()
                .movieId(movieDTO.getMovieId())
                .title(movieDTO.getTitle())
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
}
