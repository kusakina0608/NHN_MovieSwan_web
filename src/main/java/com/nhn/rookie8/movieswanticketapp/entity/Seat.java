package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(SeatId.class)
@EntityListeners(value={AuditingEntityListener.class})
public class Seat extends BaseEntity implements Persistable<SeatId> {
    @Id
    @Column(name="timetable_id", length=15, nullable=false)
    private String timetableId;
    @Id
    @Column(name="seat_code", length=4, nullable=false)
    private String seatCode;
    @Column(name="member_id", length=20, nullable=false)
    private String memberId;
    @Column(name="reservation_id", length=20, nullable=true)
    private String reservationId;

    @Transient
    @Override
    public SeatId getId() {
        return SeatId.builder()
                .timetableId(timetableId)
                .seatCode(seatCode)
                .build();
    }

    private @Transient boolean isNew = true;

    @Transient
    @Override
    public boolean isNew() {
        return true;
    }

    @PrePersist
    void markNotNew() {
        isNew = false;
    }

    public void changeReservationId(String reservationId){
        this.reservationId = reservationId;
    }
}
