package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ApiResultDTO;
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

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repository;

    @Override
    public ApiResultDTO registerQuestion(QuestionDTO dto) {
        Question entity = dtoToEntity(dto);
        repository.save(entity);

        ApiResultDTO apiResultDTO = ApiResultDTO.builder()
                .httpStatus("200")
                .error(Boolean.FALSE)
                .build();

        log.info(apiResultDTO);
        return apiResultDTO;
    }

    @Override
    public QuestionDTO readQuestion(Integer qid) {
        Optional<Question> result = repository.findById(qid);
        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getQuestionList(PageRequestDTO requestDTO, String uid) {
        Pageable pageable = requestDTO.getPageable(Sort.by("qid").descending());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QQuestion qQuestion = QQuestion.question;

        BooleanExpression expression = qQuestion.uid.eq(uid);
        booleanBuilder.and(expression);

        Page<Question> result = repository.findAll(booleanBuilder, pageable);
        Function<Question, QuestionDTO> fn = (entity) -> entityToDTO(entity);
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getQuestionListAdmin(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("qid").descending());
        Page<Question> result = repository.findAll(pageable);
        Function<Question, QuestionDTO> fn = (entity) -> entityToDTO(entity);
        return new PageResultDTO<>(result, fn);
    }
}
