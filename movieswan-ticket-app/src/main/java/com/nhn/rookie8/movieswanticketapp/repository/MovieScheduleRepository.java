package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.MovieSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, String> {
    List<MovieSchedule> findByMidOrderByTimeAsc(String mid);
}
