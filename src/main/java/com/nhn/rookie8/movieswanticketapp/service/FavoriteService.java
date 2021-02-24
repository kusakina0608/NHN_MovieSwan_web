package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;

import java.util.List;

public interface FavoriteService {
    String register(FavoriteDTO favoriteDTO);
    List<String> getList(String uid);
    boolean isFavorite(String uid, String mid);
    void remove(FavoriteDTO favoriteDTO);

    default Favorite dtoToEntity(FavoriteDTO favoriteDTO) {
        return Favorite.builder()
                .uid(favoriteDTO.getUid())
                .mid(favoriteDTO.getMid())
                .build();
    }

    default FavoriteDTO entityToDto(Favorite favorite) {
        return FavoriteDTO.builder()
                .uid(favorite.getUid())
                .mid(favorite.getMid())
                .build();
    }
}
