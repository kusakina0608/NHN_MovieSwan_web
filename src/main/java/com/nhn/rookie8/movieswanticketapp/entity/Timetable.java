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
public class Timetable extends BaseEntity{
    @Id
    @Column(name="timetable_id", length=15, nullable=false)
    private String timetableId;

    @Column(name="movie_id", length=10, nullable=false)
    private String movieId;

    @Column(name="start_time", nullable=false)
    private LocalDateTime startTime;
}
