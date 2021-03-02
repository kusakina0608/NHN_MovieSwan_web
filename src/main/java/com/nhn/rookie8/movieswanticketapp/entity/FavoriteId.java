package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteId implements Serializable {
    private String movieId;
    private String memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId favoriteId = (FavoriteId) o;
        return Objects.equals(movieId, favoriteId.movieId) && Objects.equals(memberId, favoriteId.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, memberId);
    }
}
