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
public class Favorite extends BaseEntity{
    @Id
    @Column(name="movie_id", length=10, nullable=false)
    private String movieId;
    @Id
    @Column(name="member_id", length=21, nullable=false)
    private String memberId;

}
