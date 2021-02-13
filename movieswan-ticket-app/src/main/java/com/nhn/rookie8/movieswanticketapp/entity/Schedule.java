package com.nhn.rookie8.movieswanticketapp.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class Schedule {
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
