package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteController {
    private final FavoriteService service;

    @PostMapping("/register")
    public String registerFav(FavoriteDTO favoriteDTO, HttpServletRequest request) {
        service.register(favoriteDTO);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
