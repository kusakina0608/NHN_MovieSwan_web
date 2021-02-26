package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(FavoriteId.class)
public class Favorite {
    @Id
    @Column(name = "user_id", length = 21, nullable = false)
    private String userId;

    @Id
    @Column(name = "movie_id", length = 10, nullable = false)
    private String movieId;
}
