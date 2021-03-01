package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;

public interface QuestionService {
    Integer registerQuestion(QuestionDTO dto);
    QuestionDTO readQuestion(Integer questionId);
    PageResultDTO<QuestionDTO, Question> getMyQuestionList(PageRequestDTO requestDTO, String userId);
    PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO requestDTO);

    default Question dtoToEntity(QuestionDTO dto) {
        return Question.builder()
                .questionId(dto.getQuestionId())
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }

    default QuestionDTO entityToDto(Question entity) {
        return QuestionDTO.builder()
                .questionId(entity.getQuestionId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }
}