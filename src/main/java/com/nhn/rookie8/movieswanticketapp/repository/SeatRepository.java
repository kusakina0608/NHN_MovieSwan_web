package com.nhn.rookie8.movieswanticketapp.repository;

import com.nhn.rookie8.movieswanticketapp.entity.Seat;
import com.nhn.rookie8.movieswanticketapp.entity.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface SeatRepository extends JpaRepository<Seat, SeatId>, QuerydslPredicateExecutor<Seat> {
    @Transactional
    @Modifying
    @Query("update Seat s set s.rid = :rid where s.tid = :tid AND s.sid = :sid AND s.uid = :uid ")
    int updateRid(@Param("rid") String rid, @Param("tid") String tid, @Param("sid") String sid, @Param("uid") String uid);
}
