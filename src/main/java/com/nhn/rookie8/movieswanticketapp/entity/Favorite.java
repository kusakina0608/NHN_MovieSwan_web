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
    @Column(name = "uid", length = 21, nullable = false)
    private String uid;

    @Id
    @Column(name = "mid", length = 10, nullable = false)
    private String mid;
}
