package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(SeatId.class)
public class Seat {
    @Id
    @Column(name="rid", length = 20, nullable = false)
    private String rid;
    @Id
    @Column(name="sid", length = 4, nullable = false)
    private String sid;
}
