package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;

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
public class Schedule extends BaseEntity {
    @Id
    @Column(length = 15, nullable = false)
    private String tid;

    private LocalDateTime time;

    @Column(length = 10, nullable = false)
    private String mid;

    public void changeTime(LocalDateTime changed) {
        this.time = changed;
    }
}
