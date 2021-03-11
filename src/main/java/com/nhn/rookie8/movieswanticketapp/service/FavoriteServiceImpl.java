package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.FavoriteDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Favorite;
import com.nhn.rookie8.movieswanticketapp.entity.FavoriteId;
import com.nhn.rookie8.movieswanticketapp.entity.QFavorite;
import com.nhn.rookie8.movieswanticketapp.repository.FavoriteRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteRepository repository;

    @Override
    public String addFavorite(FavoriteDTO favoriteDTO) {
        Favorite favorite = dtoToEntity(favoriteDTO);
        repository.save(favorite);

        return favorite.getMovieId();
    }

    @Override
    public List<String> getFavoriteList(String memberId) {
        Pageable pageable = PageRequest.of(0, 1000);
        BooleanBuilder booleanBuilder = favoriteListBuilder(memberId);

        Page<Favorite> result = repository.findAll(booleanBuilder, pageable);

        return result.stream().map(fav -> fav.getMovieId()).collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(String memberId, String movieId) {
        FavoriteId favoriteId = FavoriteId.builder()
                .memberId(memberId)
                .movieId(movieId)
                .build();

        return repository.findById(favoriteId).isPresent();
    }

    @Override
    public void removeFavorite(FavoriteDTO favoriteDTO) {
        FavoriteId favoriteId = FavoriteId.builder()
                .memberId(favoriteDTO.getMemberId())
                .movieId(favoriteDTO.getMovieId())
                .build();

        repository.deleteById(favoriteId);
    }

    private BooleanBuilder favoriteListBuilder(String memberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QFavorite qFavorite = QFavorite.favorite;
        booleanBuilder.and(qFavorite.memberId.eq(memberId));
        return booleanBuilder;
    }
}
