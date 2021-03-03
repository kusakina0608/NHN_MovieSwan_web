package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    String addFavorite(FavoriteDTO favoriteDTO);
    List<String> getFavoriteList(String memberId);
    boolean isFavorite(String memberId, String movieId);
    void removeFavorite(FavoriteDTO favoriteDTO);

    default Favorite dtoToEntity(FavoriteDTO favoriteDTO) {
        return Favorite.builder()
                .memberId(favoriteDTO.getMemberId())
                .movieId(favoriteDTO.getMovieId())
                .build();
    }

    default FavoriteDTO entityToDto(Favorite favorite) {
        return FavoriteDTO.builder()
                .memberId(favorite.getMemberId())
                .movieId(favorite.getMovieId())
                .regDate(favorite.getRegDate())
                .modDate(favorite.getModDate())
                .build();
    }
}
