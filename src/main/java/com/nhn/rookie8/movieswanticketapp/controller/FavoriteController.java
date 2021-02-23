package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteController {
    private final FavoriteService service;

    @PostMapping("/register")
    public void registerFavorite(FavoriteDTO favoriteDTO) {
        service.register(favoriteDTO);
    }

    @DeleteMapping("/delete")
    public void deleteFavorite(FavoriteDTO favoriteDTO) {
        service.remove(favoriteDTO);
    }

    @GetMapping("/isFavorite")
    public boolean isFavorite(String uid, String mid) {
        return service.isFavorite(uid, mid);
    }
}
