package com.nhn.rookie8.movieswanticketapp.movie;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MovieServiceTest {
    @Autowired
    private MovieService service;

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(12).build();

        PageResultDTO<MovieDTO, Movie> pageResultDTO = service.getList(pageRequestDTO, true);
        for(MovieDTO movieDTO : pageResultDTO.getDtoList()) {
            System.out.println(movieDTO);
        }
    }

    @Test
    public void testReleaseList() {
        List<MovieDTO> movieDTOList = service.getReleaseList();
        for(MovieDTO movieDTO : movieDTOList) {
            System.out.println(movieDTO);
        }
    }
}