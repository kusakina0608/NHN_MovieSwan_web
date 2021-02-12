package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reservation {
    @Column(name="tid", length = 15, nullable = false)
    private String tid;
    @Column(name="uid", length = 21, nullable = false)
    private String uid;
    @Id
    @Column(name="rid", length = 20, nullable = false)
    private String rid;
    @Column(name="totalnum", nullable = false)
    private int totalNum;
    @Column(name="price", nullable = false)
    private int price;
    @Column(name="adultnum", nullable = false)
    private int adultNum;
    @Column(name="childnum", nullable = false)
    private int childNum;
    @Column(name="oldnum", nullable = false)
    private int oldNum;
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;
}
