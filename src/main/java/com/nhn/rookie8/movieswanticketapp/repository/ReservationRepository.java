package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String>, QuerydslPredicateExecutor<Reservation> {
    @Query(value = "SELECT title FROM movie WHERE movie_id IN (SELECT DISTINCT movie_id FROM timetable WHERE timetable_id = :timetableId)", nativeQuery = true)
    String getMovieName(@Param("timetableId") String timetableId);

    @Query(value = "SELECT poster_file_name FROM movie WHERE movie_id IN (SELECT DISTINCT movie_id FROM timetable WHERE timetable_id = :timetableId)", nativeQuery = true)
    String getMoviePoster(@Param("timetableId") String timetableId);

    @Query(value = "SELECT start_time FROM timetable WHERE timetable_id = :timetableId", nativeQuery = true)
    LocalDateTime getStartTime(@Param("timetableId") String timetableId);

    @Query(value = "SELECT seat_code FROM seat WHERE reservation_id = :reservationId", nativeQuery = true)
    List<String> getSeatList(@Param("reservationId") String reservationId);
}
