package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.AnswerDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Answer;

public interface AnswerService {
    Integer registerAnswer(AnswerDTO dto);
    AnswerDTO readAnswer(Integer questionId);

    default Answer dtoToEntity(AnswerDTO dto) {
        return Answer.builder()
                .questionId(dto.getQuestionId())
                .content(dto.getContent())
                .build();
    }

    default AnswerDTO entityToDto(Answer entity) {
        return AnswerDTO.builder()
                .questionId(entity.getQuestionId())
                .content(entity.getContent())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }
}
