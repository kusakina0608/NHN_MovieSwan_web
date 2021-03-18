package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(value = { AuditingEntityListener.class })
public class AuthCheck extends BaseEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "auth_key", length = 33, nullable = false)
    private String authKey;

}
