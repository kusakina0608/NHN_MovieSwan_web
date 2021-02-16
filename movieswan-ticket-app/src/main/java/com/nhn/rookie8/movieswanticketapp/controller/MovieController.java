package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
@Log4j2
public class MovieController {
    private final MovieService service;

    @Value("${com.nhn.rookie8.upload.path}")
    private String uploadPath;

    @PostMapping("/register")
    public void registerMovie(MovieDTO movieDTO, @RequestParam("uploadFile") MultipartFile uploadFile, HttpServletResponse response) throws IOException  {
        log.info(movieDTO.getName());
        log.info(uploadFile.getName());

        if(uploadFile.getContentType().startsWith("image") == false) {
            log.warn("this file is not image type");
            return;
        }

        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        String folderPath = makeFolder();
        String saveName = uploadPath + File.separator + folderPath + File.separator + "_" + fileName;
        Path savePath = Paths.get(saveName);

        try {
            uploadFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        movieDTO.setPoster(savePath.toString());
        service.register(movieDTO);

        response.sendRedirect("/admin");
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
