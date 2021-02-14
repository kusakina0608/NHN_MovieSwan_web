package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private String mid;
    private String name;
    private String poster;
    private String director;
    private String actor;
    private int runtime;
    private String genre;
    private String story;
    private LocalDate startdate;
    private LocalDate enddate;
}
