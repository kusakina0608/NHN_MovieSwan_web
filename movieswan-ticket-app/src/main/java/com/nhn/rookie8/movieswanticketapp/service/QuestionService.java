package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;

public interface QuestionService {
    void registerQuestion(QuestionDTO dto);

    default Question dtoToEntity(QuestionDTO dto) {
        Question entity = Question.builder()
                .qid(dto.getQid())
                .uid(dto.getUid())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return entity;
    }

    default QuestionDTO entityToDTO(Question entity) {
        QuestionDTO questionDTO = QuestionDTO.builder()
                .qid(entity.getQid())
                .uid(entity.getUid())
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
        return questionDTO;
    }
}
