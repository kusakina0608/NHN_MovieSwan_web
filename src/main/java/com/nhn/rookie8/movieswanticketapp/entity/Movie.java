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
    @Column(name = "movie_id", length = 10, nullable = false)
    private String movieId;

    @Column(name = "movie_title", length = 50, nullable = false)
    private String movieTitle;

    @Column(name = "poster", length = 100, nullable = false)
    private String poster;

    @Column(name = "director", length = 20, nullable = false)
    private String director;

    @Column(name = "actor", length = 50, nullable = false)
    private String actor;

    @Column(name = "runtime", nullable = false)
    private Integer runtime;

    @Column(name = "genre", length = 20, nullable = false)
    private String genre;

    @Column(name = "story", length = 1500)
    private String story;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
