package com.nhn.rookie8.movieswanticketapp.entity;

import java.io.Serializable;
import java.util.Objects;

public class SeatId implements Serializable {
    private String rid;
    private String sid;

    public SeatId() {
    }

    public SeatId(String rid, String sid) {
        this.rid = rid;
        this.sid = sid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatId seatId = (SeatId) o;
        return Objects.equals(rid, seatId.rid) && Objects.equals(sid, seatId.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rid, sid);
    }
}
