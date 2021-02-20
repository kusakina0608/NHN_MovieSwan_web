package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.entity.*;
import com.nhn.rookie8.movieswanticketapp.repository.FavoriteRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteRepository repository;

    @Override
    public String register(FavoriteDTO favoriteDTO) {
        Favorite favorite = dtoToEntity(favoriteDTO);
        repository.save(favorite);

        return favorite.getMid();
    }

    @Override
    public PageResultDTO<FavoriteDTO, Favorite> getList(PageRequestDTO pageRequestDTO, String uid) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("mid"));
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QFavorite qFavorite = QFavorite.favorite;
        BooleanExpression expression = qFavorite.uid.eq(uid);
        booleanBuilder.and(expression);

        Page<Favorite> result = repository.findAll(booleanBuilder, pageable);

        Function<Favorite, FavoriteDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public boolean isFavorite(String uid, String mid) {
        Pageable pageable = PageRequest.of(0, (int) repository.count());
        FavoriteId favoriteId = FavoriteId.builder()
                .uid(uid)
                .mid(mid)
                .build();

        Optional<Favorite> result = repository.findById(favoriteId);

        return result.isPresent();
    }

    @Override
    public void remove(FavoriteDTO favoriteDTO) {
        FavoriteId favoriteId = FavoriteId.builder()
                .uid(favoriteDTO.getUid())
                .mid(favoriteDTO.getMid())
                .build();

        repository.deleteById(favoriteId);
    }
}
