package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QQuestion;
import com.nhn.rookie8.movieswanticketapp.entity.Question;
import com.nhn.rookie8.movieswanticketapp.repository.QuestionRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service

@RequiredArgsConstructor

@Log4j2
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repository;

    @Override
    public void registerQuestion(QuestionDTO dto) {
        try {
            Question entity = dtoToEntity(dto);
            repository.save(entity);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public QuestionDTO readQuestion(Integer qid) {
        try {
            Optional<Question> result = repository.findById(qid);
            return result.isPresent() ? entityToDTO(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getMyQuestionList(PageRequestDTO requestDTO, String uid) {
        try {
            Pageable pageable = requestDTO.getPageable(Sort.by("qid").descending());
            BooleanBuilder booleanBuilder = getUserInfo(uid);
            Page<Question> result = repository.findAll(booleanBuilder, pageable);
            Function<Question, QuestionDTO> fn = (entity) -> entityToDTO(entity);
            return new PageResultDTO<>(result, fn);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("qid").descending());
        Page<Question> result = repository.findAll(pageable);
        Function<Question, QuestionDTO> fn = (entity) -> entityToDTO(entity);
        return new PageResultDTO<>(result, fn);
    }

    private BooleanBuilder getUserInfo(String uid) {
        try {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            QQuestion qQuestion = QQuestion.question;

            BooleanExpression expression = qQuestion.uid.eq(uid);
            booleanBuilder.and(expression);
            return booleanBuilder;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
