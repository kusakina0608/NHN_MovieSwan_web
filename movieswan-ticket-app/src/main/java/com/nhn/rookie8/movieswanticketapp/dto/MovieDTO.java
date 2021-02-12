package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
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
