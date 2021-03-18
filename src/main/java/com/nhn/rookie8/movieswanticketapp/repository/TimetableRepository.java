package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, String>, QuerydslPredicateExecutor<Timetable> {
    List<Timetable> findByMovieIdOrderByStartTimeAsc(String movieId);
    @Query("SELECT COUNT(t.timetableId) > 0 FROM Timetable t WHERE t.movieId =:movieId AND t.startTime > :currentTime")
    boolean existsByMovieId(String movieId, LocalDateTime currentTime);
}
