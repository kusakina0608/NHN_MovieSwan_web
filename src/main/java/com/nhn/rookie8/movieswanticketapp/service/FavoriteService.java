package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    String registerFavorite(FavoriteDTO favoriteDTO);
    List<String> getFavoriteList(String uid);
    boolean isFavorite(String uid, String mid);
    void removeFavorite(FavoriteDTO favoriteDTO);

    default Favorite dtoToEntity(FavoriteDTO favoriteDTO) {
        return Favorite.builder()
                .userId(favoriteDTO.getUserId())
                .movieId(favoriteDTO.getMovieId())
                .build();
    }

    default FavoriteDTO entityToDto(Favorite favorite) {
        return FavoriteDTO.builder()
                .userId(favorite.getUserId())
                .movieId(favorite.getMovieId())
                .build();
    }
}
