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
    @Id
    @Column(name="rid", length = 20, nullable = false)
    private String rid;
    @Column(name="tid", length = 15, nullable = false)
    private String tid;
    @Column(name="uid", length = 21, nullable = false)
    private String uid;
    @Column(name="childnum", nullable = false)
    private int childNum;
    @Column(name="adultnum", nullable = false)
    private int adultNum;
    @Column(name="oldnum", nullable = false)
    private int oldNum;
    @Column(name="totalnum", nullable = false)
    private int totalNum;
    @Column(name="price", nullable = false)
    private int price;
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;
}
