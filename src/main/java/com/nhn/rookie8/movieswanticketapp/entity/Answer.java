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
@EntityListeners(value = {AuditingEntityListener.class})
public class Answer extends BaseEntity {
    @Id
    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Column(name = "content", length = 1500, nullable = false)
    private String content;
}