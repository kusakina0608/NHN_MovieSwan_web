package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailDTO {
    private String movieId;
    private String title;
    private String director;
    private String actor;
    private int runtime;
    private float rating;
    private boolean reservationAvailable;
    private String genre;
    private String story;
    private String poster;
    private LocalDate startDate;
    private LocalDate endDate;
}
