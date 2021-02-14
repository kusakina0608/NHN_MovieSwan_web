package com.nhn.rookie8.movieswanticketapp;

import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieEntityTest {
    @Autowired
    private MovieRepository movieRepository;

    @Commit
    @Transactional
    @Test
    public void registerMovie() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Movie movie = Movie.builder()
                    .mid("111111" + i)
                    .name("movie name" + i)
                    .genre("genre")
                    .poster("poster" + i + ".jpg")
                    .startdate(LocalDate.of(2011, 11, 11))
                    .enddate(LocalDate.of(2011, 12, 25))
                    .runtime(100)
                    .story("stooooory" + i)
                    .actor("actor" + i)
                    .director("director" + i)
                    .build();

            movieRepository.save(movie);
        });

    }

}
