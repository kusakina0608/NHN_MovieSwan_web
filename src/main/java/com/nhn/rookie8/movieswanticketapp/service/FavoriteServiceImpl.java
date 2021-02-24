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
    public String register(FavoriteDTO favoriteDTO) {
        Favorite favorite = dtoToEntity(favoriteDTO);
        repository.save(favorite);

        return favorite.getMid();
    }

    @Override
    public List<String> getList(String uid) {
        Pageable pageable = PageRequest.of(0, (int) repository.count());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QFavorite qFavorite = QFavorite.favorite;
        BooleanExpression expression = qFavorite.uid.eq(uid);
        booleanBuilder.and(expression);

        Page<Favorite> result = repository.findAll(booleanBuilder, pageable);

        List<String> midList = new ArrayList<>();
        for(Favorite fav : result)
            midList.add(fav.getMid());

        return midList;
    }

    @Override
    public boolean isFavorite(String uid, String mid) {
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
