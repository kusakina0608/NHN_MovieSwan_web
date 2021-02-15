package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, SeatId> {
}
