package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;

public interface QuestionService {
    void registerQuestion(QuestionDTO dto);
    QuestionDTO readQuestion(Integer qid);
    PageResultDTO<QuestionDTO, Question> getMyQuestionList(PageRequestDTO requestDTO, String uid);
    PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO requestDTO);

    default Question dtoToEntity(QuestionDTO dto) {
        return Question.builder()
                .qid(dto.getQid())
                .uid(dto.getUid())
                .title(dto.getTitle())
                .content(dto.getContent())
                .regdate(dto.getRegdate())
                .build();
    }

    default QuestionDTO entityToDTO(Question entity) {
        return QuestionDTO.builder()
                .qid(entity.getQid())
                .uid(entity.getUid())
                .title(entity.getTitle())
                .content(entity.getContent())
                .regdate(entity.getRegdate())
                .build();
    }
}