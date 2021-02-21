package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, String>, QuerydslPredicateExecutor<Reservation> {
    @Query(value = "SELECT mid FROM movie WHERE mid IN (SELECT DISTINCT mid FROM movie_schedule as ms WHERE ms.tid = ?1);", nativeQuery = true)
    String getMovieMid(@Param("tid") String tid);

    @Query(value = "SELECT name FROM movie WHERE mid IN (SELECT DISTINCT mid FROM movie_schedule as ms WHERE ms.tid = ?1);", nativeQuery = true)
    String getMovieName(@Param("tid") String tid);

    @Query(value = "SELECT poster FROM movie WHERE mid IN (SELECT DISTINCT mid FROM movie_schedule as ms WHERE ms.tid = ?1);", nativeQuery = true)
    String getMoviePoster(@Param("tid") String tid);
}
