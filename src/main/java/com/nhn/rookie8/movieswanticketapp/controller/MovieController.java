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

    static final char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '_', '*'
    };

    @PostMapping("/register")
    public String registerMovie(MovieDTO movieDTO, @RequestParam("uploadFile") MultipartFile uploadFile,
                              HttpServletRequest request) throws IOException  {
        log.info(movieDTO.getName());
        log.info(uploadFile.getName());
        String posterPath;

        Path savePath;

        try {
            if (uploadFile.getContentType() == null ||
                    !uploadFile.getContentType().startsWith("image"))
                throw new Exception("이미지 타입의 파일이 아닙니다.");
        } catch (Exception e) {
            log.error("uploadfile type : {}", uploadFile.getContentType(), e);
            return "redirect:/admin";
        }

        log.info(uploadPath);
        UUID uuid = UUID.randomUUID();
        String uuidStr = toUnsignedString(uuid.getMostSignificantBits(), 6) + toUnsignedString(uuid.getLeastSignificantBits(), 6);
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
        posterPath = makeFolder() + File.separator + uuidStr + extensionName;
        log.info(posterPath);
        String saveName = uploadPath + File.separator + posterPath;
        savePath = Paths.get(saveName);

        try {
            uploadFile.transferTo(savePath);
        } catch (IOException e) {
            log.error("파일 업로드에 문제가 발생했습니다. {}", uploadFile, e);
        }

        movieDTO.setPoster(posterPath);
        service.register(movieDTO);

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
        return service.read(mid);
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists())
            uploadPathFolder.mkdirs();

        return folderPath;
    }

    private static String toUnsignedString(long i, int shift) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = (long)radix - 1;
        long number = i;
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= shift;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }


}
