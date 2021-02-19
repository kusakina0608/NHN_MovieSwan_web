package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;

public interface FavoriteService {
    String register(FavoriteDTO favoriteDTO);
    PageResultDTO<FavoriteDTO, Favorite> getList(PageRequestDTO pageRequestDTO, String uid);
    void remove(FavoriteDTO favoriteDTO);

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
