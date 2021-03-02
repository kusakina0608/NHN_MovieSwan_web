package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(value={AuditingEntityListener.class})
public class Reservation extends BaseEntity{
    @Id
    @Column(name="reservation_id", length=20, nullable=false)
    private String reservationId;
    @Column(name="timetable_id", length=15, nullable=false)
    private String timetableId;
    @Column(name="member_id", length=21, nullable=false)
    private String memberId;
    @Column(name="young_num", nullable=false)
    private int youngNum;
    @Column(name="adult_num", nullable=false)
    private int adultNum;
    @Column(name="elder_num", nullable=false)
    private int elderNum;
    @Column(name="total_num", nullable=false)
    private int totalNum;
    @Column(name="price", nullable=false)
    private int price;
    @CreatedDate
    @Column(name="pay_date", updatable=false)
    private LocalDateTime payDate;
}
