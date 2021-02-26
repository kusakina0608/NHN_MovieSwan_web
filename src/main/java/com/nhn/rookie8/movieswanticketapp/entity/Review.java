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
@EntityListeners(value = {AuditingEntityListener.class})
public class Review {
    @Id
    @Column(name = "review_id", length = 15, nullable = false)
    private String reviewId;

    @Column(name = "movie_id", length = 10, nullable = false)
    private String movieId;

    @Column(name = "user_id", length = 21, nullable = false)
    private String userId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "review_content", length = 500)
    private String reviewContent;

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regdate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime moddate;

    public void changeGrade(int rating) {
        this.rating = rating;
    }

    public void changeContent(String content) {
        this.reviewContent = content;
    }
}
