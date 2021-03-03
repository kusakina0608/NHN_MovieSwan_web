package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import com.nhn.rookie8.movieswanticketapp.service.AuthService;
import com.nhn.rookie8.movieswanticketapp.service.ImageService;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
@Log4j2
public class MovieController {
    private final MovieService movieService;
    private final ReviewService reviewService;

    final String storageUrl = "https://api-storage.cloud.toast.com/v1/AUTH_444cc4c997a54c939f2e34c5b4572193";
    final String containerName = "poster";

    final String authUrl = "https://api-identity.infrastructure.cloud.toast.com/v2.0";
    final String tenantId = "444cc4c997a54c939f2e34c5b4572193";
    final String username = "pkw1012@nhn.com";
    final String password = "fnzlqorwh";

    private String uploadPath = System.getProperty("user.dir") + "/images";

    @GetMapping({"", "/"})
    public String moviePage() {
        return "redirect:/movie/list?current=true";
    }

    @GetMapping("/list")
    public String currentMovieList(PageRequestDTO pageRequestDTO, boolean current, Model model) {
        PageResultDTO<MovieDTO, Movie> resultDTO = movieService.getMoviePage(pageRequestDTO, current);

        model.addAttribute("result", resultDTO);
        model.addAttribute("current", current);
        return "/page/movie_list";
    }

    @GetMapping("/detail")
    public String movieDetail(String movieId, PageRequestDTO reviewRequestDTO, HttpServletRequest httpServletRequest, Model model) {
        MovieDTO movieDTO = movieService.getMovieDetail(movieId);

        String memberId = (String) httpServletRequest.getAttribute("memberId");

        model.addAttribute("dto", movieDTO);
        model.addAttribute("reviews", reviewService.getReviewPage(reviewRequestDTO, movieId));
        model.addAttribute("my_review", reviewService.findMyReviewByMovieId(movieId, memberId));
        return "/page/movie_detail";
    }

    @PostMapping("/register")
    public String registerMovie(MovieDTO movieDTO, @RequestParam("uploadFile") MultipartFile uploadFile) {
        AuthService authService = new AuthService(authUrl, tenantId, username, password);
        String token = authService.requestToken();
        log.info(token);

        ImageService imageService = new ImageService(storageUrl, token);
        String posterName;

        log.info(uploadPath);

        try {
            posterName = imageService.uploadImage(containerName, uploadFile);
            log.info("파일 업로드 성공 {}", uploadFile.getOriginalFilename());
        } catch (Exception e) {
            log.warn("파일 업로드 실패 {}", uploadFile.getOriginalFilename(), e);
            return "redirect:/admin";
        }

        movieDTO.setPoster(posterName);
        movieService.registerMovie(movieDTO);

        return "redirect:/admin";
    }

    @ResponseBody
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        AuthService authService = new AuthService(authUrl, tenantId, username, password);
        String token = authService.requestToken();
        log.info(token);

        ImageService imageService = new ImageService(storageUrl, token);
        ResponseEntity<byte[]> result = imageService.displayImage(containerName, fileName);

        return result;
    }

    @ResponseBody
    @GetMapping("/getMovieInfo")
    public MovieDTO getMovieInfo(String movieId) {
        return movieService.getMovieDetail(movieId);
    }
}
