package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;

public interface QuestionService {
    Integer registerQuestion(QuestionDTO dto);
    QuestionDTO readQuestion(Integer questionId);
    PageResultDTO<QuestionDTO, Question> getMyQuestionList(PageRequestDTO requestDTO, String memberId);
    PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO requestDTO);

    default Question dtoToEntity(QuestionDTO questionDTO) {
        return Question.builder()
                .questionId(questionDTO.getQuestionId())
                .memberId(questionDTO.getMemberId())
                .title(questionDTO.getTitle())
                .content(questionDTO.getContent())
                .build();
    }

    default QuestionDTO entityToDTO(Question question) {
        return QuestionDTO.builder()
                .questionId(question.getQuestionId())
                .memberId(question.getMemberId())
                .title(question.getTitle())
                .content(question.getContent())
                .regDate(question.getRegDate())
                .modDate(question.getModDate())
                .build();
    }
}