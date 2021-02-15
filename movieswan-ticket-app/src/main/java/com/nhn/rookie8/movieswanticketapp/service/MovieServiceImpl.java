package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{
    private final MovieRepository repository;

    @Override
    public PageResultDTO<MovieDTO, Movie> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("mid").descending());

        Page<Movie> result = repository.findAll(pageable);

        Function<Movie, MovieDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<MovieDTO> getReleaseList() {
        List<Movie> movieList = repository.findAll();
        List<MovieDTO> movieDTOList = new ArrayList<MovieDTO>();

        for (Movie movie: movieList)
            movieDTOList.add(entityToDTO(movie));

        return movieDTOList;
    }
}
