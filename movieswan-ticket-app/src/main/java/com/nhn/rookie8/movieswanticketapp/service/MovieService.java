package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;

import java.util.List;

public interface MovieService {
    List<MovieDTO> getReleaseMovieList();
}