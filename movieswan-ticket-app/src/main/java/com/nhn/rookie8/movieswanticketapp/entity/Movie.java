package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Movie{
    @Id
    @Column(length = 10, nullable = false)
    private String mid;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String poster;

    @Column(length = 20, nullable = false)
    private String director;

    @Column(length = 50, nullable = false)
    private String actor;

    @Column(nullable = false)
    private Integer runtime;

    @Column(length = 20, nullable = false)
    private String genre;

    @Column(length = 1500, nullable = false)
    private String story;

    @Column(nullable = false)
    private LocalDate startdate;

    @Column(nullable = false)
    private LocalDate enddate;
}
