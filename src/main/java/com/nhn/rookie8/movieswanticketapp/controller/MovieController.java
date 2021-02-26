package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/movie")
@Log4j2
public class MovieController {
    private final MovieService service;

    private String uploadPath = System.getProperty("user.dir") + "/images";

    @PostMapping("/register")
    public String registerMovie(MovieDTO movieDTO, @RequestParam("uploadFile") MultipartFile uploadFile,
                              HttpServletRequest request) {
        log.info(movieDTO.getName());
        log.info(uploadFile.getName());
        String posterPath;

        Path savePath;

        try {
            String contentType = uploadFile.getContentType();
            if(contentType == null)
                throw new NullPointerException();

            if (!contentType.startsWith("image"))
                throw new Exception("이미지 타입의 파일이 아닙니다.");
        } catch (Exception e) {
            log.error("upload file type : {}", uploadFile.getContentType(), e);
            return "redirect:/admin";
        }

        log.info(uploadPath);
        String uuid = UUID.randomUUID().toString();
        String originalName = uploadFile.getOriginalFilename();
        String extensionName;
        try {
            if(originalName == null)
                throw new NullPointerException();
            extensionName = originalName.substring(originalName.lastIndexOf("."));
        } catch (NullPointerException e) {
            log.error("잘못된 파일 이름입니다.");
            return "redirect:/admin";
        }
        posterPath = makeFolder() + File.separator + uuid + extensionName;
        log.info(posterPath);
        String saveName = uploadPath + File.separator + posterPath;
        savePath = Paths.get(saveName);

        try {
            uploadFile.transferTo(savePath);
        } catch (IOException e) {
            log.error("파일 업로드에 문제가 발생했습니다. {}", uploadFile, e);
        }

        movieDTO.setPoster(posterPath);
        service.registerMovie(movieDTO);

        return "redirect:/admin";
    }

    @ResponseBody
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileName: " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            header.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error("이미지를 띄우는데 문제가 발생했습니다.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/getMovieInfo")
    public MovieDTO getMovieInfo(String mid) {
        return service.getMovieDetail(mid);
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists())
            uploadPathFolder.mkdirs();

        return folderPath;
    }
}
