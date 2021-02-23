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
    private String uid;
    private String mid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId favoriteId = (FavoriteId) o;
        return Objects.equals(uid, favoriteId.uid) && Objects.equals(mid, favoriteId.mid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, mid);
    }
}
