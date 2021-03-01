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
@EntityListeners(value = {AuditingEntityListener.class})
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", length = 11, nullable = false)
    private Integer questionId;

    @Column(name = "user_id", length = 21, nullable = false)
    private String userId;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "content", length = 1500, nullable = false)
    private String content;
}