package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MovieServiceImpl implements MovieService{
    @Override
    public List<MovieDTO> getReleaseMovieList() {
        // TODO: Request to API to get data
        // List<MovieDTO> movieList = ......

        /**************************** for generating sample data ****************************/
        List<MovieDTO> movieList = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i -> {
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 4) {
                int index = (int) (rnd.nextFloat() * alphabet.length());
                salt.append(alphabet.charAt(index));
            }
            String saltStr = salt.toString();
            MovieDTO dto = MovieDTO.builder()
                    .mid(salt + "-" + String.format("%04d", i))
                    .name("서새봄의 게임채널" + String.format("%4d", i))
                    .poster("/asset/image/sample_movie.png")
                    .director("Daejin-Lee")
                    .actor("Hyerin-Yoo")
                    .runtime(200)
                    .genre("Romance")
                    .story("하하호호 샘플 영화 스토리입니다~~~\n행복하게 살았읍니다~~")
                    .startdate(LocalDate.now())
                    .enddate(LocalDate.now().plusDays(14))
                    .build();
            return dto;
        }).collect(Collectors.toList());
        /**************************** for generating sample data ****************************/

        return movieList;
    }
}
