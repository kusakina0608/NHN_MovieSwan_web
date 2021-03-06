package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatId implements Serializable {
    private String timetableId;
    private String seatCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatId seatId = (SeatId) o;
        return Objects.equals(timetableId, seatId.timetableId) && Objects.equals(seatCode, seatId.seatCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timetableId, seatCode);
    }
}
