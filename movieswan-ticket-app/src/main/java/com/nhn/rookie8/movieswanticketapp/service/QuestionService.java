package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ApiResultDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;

public interface QuestionService {
    ApiResultDTO registerQuestion(QuestionDTO dto);
    QuestionDTO readQuestion(Integer qid);
    PageResultDTO<QuestionDTO, Question> getQuestionList(PageRequestDTO requestDTO);
    PageResultDTO<QuestionDTO, Question> getQuestionListAdmin(PageRequestDTO requestDTO);

    default Question dtoToEntity(QuestionDTO dto) {
        Question entity = Question.builder()
                .qid(dto.getQid())
                .uid(dto.getUid())
                .title(dto.getTitle())
                .content(dto.getContent())
                .regdate(dto.getRegdate())
                .build();
        return entity;
    }

    default QuestionDTO entityToDTO(Question entity) {
        QuestionDTO questionDTO = QuestionDTO.builder()
                .qid(entity.getQid())
                .uid(entity.getUid())
                .title(entity.getTitle())
                .content(entity.getContent())
                .regdate(entity.getRegdate())
                .build();
        return questionDTO;
    }
}