package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;
import com.nhn.rookie8.movieswanticketapp.entity.FavoriteId;

public interface FavoriteService {
    String register(FavoriteDTO favoriteDTO);

    default Favorite dtoToEntity(FavoriteDTO favoriteDTO) {
        Favorite favorite = Favorite.builder()
                .uid(favoriteDTO.getUid())
                .mid(favoriteDTO.getMid())
                .build();

        return favorite;
    }

    default FavoriteDTO entityToDto(Favorite favorite) {
        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .uid(favorite.getUid())
                .mid(favorite.getMid())
                .build();

        return favoriteDTO;
    }
}
