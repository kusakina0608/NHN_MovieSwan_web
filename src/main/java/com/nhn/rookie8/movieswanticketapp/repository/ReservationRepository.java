package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String>, QuerydslPredicateExecutor<Reservation> {
    @Query(value = "SELECT name FROM movie WHERE mid IN (SELECT DISTINCT mid FROM movie_schedule as ms WHERE ms.tid = :tid);", nativeQuery = true)
    String getMovieName(@Param("tid") String tid);

    @Query(value = "SELECT time FROM movie_schedule WHERE tid = :tid", nativeQuery = true)
    LocalDateTime getStartTime(@Param("tid") String tid);

    @Query(value = "SELECT sid FROM seat WHERE rid = :rid", nativeQuery = true)
    List<String> getSeatList(@Param("rid") String rid);
}
