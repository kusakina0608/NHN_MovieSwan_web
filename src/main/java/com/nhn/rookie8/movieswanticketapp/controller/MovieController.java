package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import com.nhn.rookie8.movieswanticketapp.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
@Component
@Log4j2
public class MovieController {
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final StorageService storageService;
    
    @GetMapping({"", "/"})
    public String moviePage() {
        return "redirect:/movie/list?current=true";
    }

    @GetMapping("/list")
    public String currentMovieList(@ModelAttribute PageRequestDTO pageRequestDTO, @RequestParam boolean current, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getMoviePage(pageRequestDTO, current);
        for(MovieDTO dto : resultDTO.getDtoList())
            log.info(dto);

        model.addAttribute("result", resultDTO);
        model.addAttribute("current", current);
        return "/page/movie_list";
    }

    @GetMapping("/detail")
    public String movieDetail(@RequestParam String movieId, @ModelAttribute PageRequestDTO reviewRequestDTO, Model model) {
        MovieDTO movieDTO = movieService.getMovieDetail(movieId);

        model.addAttribute("dto", movieDTO);
        model.addAttribute("reviews", reviewService.getReviewPage(reviewRequestDTO, movieId));
        return "/page/movie_detail";
    }

    @PostMapping("/register")
    public String registerMovie(@ModelAttribute MovieDTO movieDTO, @RequestParam MultipartFile uploadFile) {
        try {
            String posterName = storageService.uploadImage(uploadFile);
            movieDTO.setPoster(posterName);
            log.info("파일 업로드 성공 {}", uploadFile.getOriginalFilename());
        } catch (Exception e) {
            log.warn("파일 업로드 실패 {}", uploadFile.getOriginalFilename(), e);
            return "redirect:/admin";
        }

        movieService.registerMovie(movieDTO);

        return "redirect:/admin";
    }

    @ResponseBody
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(@RequestParam String fileName) {
        return storageService.displayImage(fileName);
    }

    @ResponseBody
    @GetMapping("/getMovieInfo")
    public MovieDTO getMovieInfo(@RequestParam String movieId) {
        return movieService.getMovieDetail(movieId);
    }
}
