package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, String> {
}
