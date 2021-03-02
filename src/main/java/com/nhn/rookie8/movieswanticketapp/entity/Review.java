package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(value={AuditingEntityListener.class})
public class Review extends BaseEntity{
    @Id
    @Column(name="review_id", length=15, nullable=false)
    private String reviewId;

    @Column(name="movie_id", length=10, nullable=false)
    private String movieId;

    @Column(name="member_id", length=21, nullable=false)
    private String memberId;

    @Column(name="rating", nullable=false)
    private int rating;

    @Column(name="content", length=500, nullable=false)
    private String content;

    public void changeRating(int rating) {
        this.rating = rating;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
