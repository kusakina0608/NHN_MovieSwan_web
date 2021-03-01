package com.nhn.rookie8.movieswanticketapp.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EntityListeners(value={AuditingEntityListener.class})
public class Question extends BaseEntity {
    @Id
    @Column(name="question_id", nullable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer questionId;

    @Column(name="member_id", length=21, nullable=false)
    private String memberId;

    @Column(name="title", length=50, nullable=false)
    private String title;

    @Column(name="content", length =1500, nullable=false)
    private String content;
}
