package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(SeatId.class)
@EntityListeners(value = {AuditingEntityListener.class})
public class Seat implements Persistable<SeatId> {
    @Id
    @Column(name="tid", length = 15, nullable = false)
    private String tid;
    @Id
    @Column(name="sid", length = 4, nullable = false)
    private String sid;
    @Column(name="uid", length = 20, nullable = false)
    private String uid;
    @Column(name="rid", length = 20, nullable = true)
    private String rid;
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;

    @Transient
    @Override
    public SeatId getId() {
        return SeatId.builder().tid(tid).sid(sid).build();
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
}
