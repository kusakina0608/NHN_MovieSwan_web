package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;

public interface MovieService {
    String register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Movie> getList(PageRequestDTO pageRequestDTO);

    MovieDTO read(String mid);

    void modify(MovieDTO movieDTO);
}
