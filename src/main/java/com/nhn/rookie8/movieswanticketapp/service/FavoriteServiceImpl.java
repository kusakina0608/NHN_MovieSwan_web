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
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QFavorite qFavorite = QFavorite.favorite;
        BooleanExpression expression = qFavorite.memberId.eq(memberId);
        booleanBuilder.and(expression);

        Page<Favorite> result = repository.findAll(booleanBuilder, pageable);

        List<String> movieIdList = new ArrayList<>();
        for(Favorite fav : result)
            movieIdList.add(fav.getMovieId());

        return movieIdList;
    }

    @Override
    public boolean isFavorite(String memberId, String movieId) {
        FavoriteId favoriteId = FavoriteId.builder()
                .memberId(memberId)
                .movieId(movieId)
                .build();

        Optional<Favorite> result = repository.findById(favoriteId);

        return result.isPresent();
    }

    @Override
    public void removeFavorite(FavoriteDTO favoriteDTO) {
        FavoriteId favoriteId = FavoriteId.builder()
                .memberId(favoriteDTO.getMemberId())
                .movieId(favoriteDTO.getMovieId())
                .build();

        repository.deleteById(favoriteId);
    }
}
