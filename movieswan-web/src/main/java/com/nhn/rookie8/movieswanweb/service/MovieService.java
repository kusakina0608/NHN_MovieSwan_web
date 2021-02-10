package com.nhn.rookie8.movieswanweb.service;

import com.nhn.rookie8.movieswanweb.dto.MovieDTO;

import java.util.List;

public interface MovieService {
    List<MovieDTO> getReleaseMovieList();
}
