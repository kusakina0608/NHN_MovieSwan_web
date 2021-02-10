package com.nhn.rookie8.movieswanweb;

import com.nhn.rookie8.movieswanweb.dto.MovieDTO;
import com.nhn.rookie8.movieswanweb.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BookingControllerTests {
    @Autowired
    private MovieService movieService;

    @Test
    public void testGetReleaseMovieList(){
        List<MovieDTO> movieList = movieService.getReleaseMovieList();
        movieList.forEach(el -> {
            System.out.println(el);
        });
    }
}
