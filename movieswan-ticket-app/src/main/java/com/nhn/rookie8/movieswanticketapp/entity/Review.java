package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {
    @Id
    @Column(name = "rid", length = 15, nullable = false)
    private String rid;

    @Column(name = "mid", length = 10, nullable = false)
    private String mid;

    @Column(name = "uid", length = 21, nullable = false)
    private String uid;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "regdate", nullable = false)
    private LocalDateTime regdate;

    @LastModifiedDate
    @Column(name = "moddate", nullable = false)
    private LocalDateTime moddate;

}
