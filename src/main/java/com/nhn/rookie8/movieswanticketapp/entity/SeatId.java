package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

@Builder
public class SeatId implements Serializable {
    private String tid;
    private String sid;

    public SeatId() {
    }

    public SeatId(String tid, String sid) {
        this.tid = tid;
        this.sid = sid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatId seatId = (SeatId) o;
        return Objects.equals(tid, seatId.tid) && Objects.equals(sid, seatId.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tid, sid);
    }
}
