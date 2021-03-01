package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String>, QuerydslPredicateExecutor<Reservation> {
    @Query(value = "SELECT title FROM movie WHERE movie_id IN (SELECT DISTINCT movie_id FROM timetable WHERE timetable.timetable_id = :tid);", nativeQuery = true)
    String getMovieName(@Param("tid") String tid);

    @Query(value = "SELECT time FROM timetable WHERE timetable_id = :tid", nativeQuery = true)
    LocalDateTime getStartTime(@Param("tid") String tid);

    @Query(value = "SELECT seat_code FROM seat WHERE reservation_id = :rid", nativeQuery = true)
    List<String> getSeatList(@Param("rid") String rid);
}
