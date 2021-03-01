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
    public Integer registerQuestion(QuestionDTO dto) {
        try {
            Question entity = dtoToEntity(dto);
            repository.save(entity);

            log.info("Registered Entity : {}", entity);

            return entity.getQuestionId();
        } catch (Exception e) {
            log.error(e);
            return -1;
        }
    }

    @Override
    public QuestionDTO readQuestion(Integer questionId) {
        try {
            Optional<Question> result = repository.findById(questionId);

            log.info("Search Result : {}", result.isPresent() ? result : "No Result");

            return result.isPresent() ? entityToDTO(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getMyQuestionList(PageRequestDTO requestDTO, String memberId) {
        try {
            Pageable pageable = requestDTO.getPageable(Sort.by("questionId").descending());
            BooleanBuilder booleanBuilder = getUserInfo(memberId);
            Page<Question> result = repository.findAll(booleanBuilder, pageable);

            log.info("Search Results : {}", result);

            Function<Question, QuestionDTO> fn = (entity -> entityToDTO(entity));
            return new PageResultDTO<>(result, fn);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getAllQuestionList(PageRequestDTO requestDTO) {
        try {
            Pageable pageable = requestDTO.getPageable(Sort.by("questionId").descending());
            Page<Question> result = repository.findAll(pageable);
            Function<Question, QuestionDTO> fn = (entity -> entityToDTO(entity));
            return new PageResultDTO<>(result, fn);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    private BooleanBuilder getUserInfo(String memberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QQuestion qQuestion = QQuestion.question;
        BooleanExpression expression = qQuestion.memberId.eq(memberId);
        booleanBuilder.and(expression);
        return booleanBuilder;
    }
}