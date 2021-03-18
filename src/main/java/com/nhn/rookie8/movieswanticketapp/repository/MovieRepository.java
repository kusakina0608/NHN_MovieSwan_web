package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MovieRepository extends JpaRepository<Movie, String>, QuerydslPredicateExecutor<Movie> {
    @Query(value = "SELECT AVG(rating) FROM review WHERE movie_id = :movieId", nativeQuery = true)
    Float getAverageRating(@Param("movieId") String movieId);

    @Query(value = "SELECT COUNT(timetable_id) FROM timetable WHERE " +
            "movie_id = :movieId AND start_time > :currentTime", nativeQuery = true)
    Integer getTimetableNum(@Param("movieId") String movieId, @Param("currentTime") LocalDateTime currentTime);
}
