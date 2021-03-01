package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.AnswerDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Answer;
import com.nhn.rookie8.movieswanticketapp.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository repository;

    @Override
    public Integer registerAnswer(AnswerDTO dto) {
        try {
            Answer entity = dtoToEntity(dto);
            repository.save(entity);

            log.info("Registered Entity : {}", entity);

            return entity.getQuestionId();
        } catch (Exception e) {
            log.error(e);
            return -1;
        }
    }

    @Override
    public AnswerDTO readAnswer(Integer questionId) {
        try {
            Optional<Answer> result = repository.findById(questionId);

            log.info("Search Result : {}", result.isPresent() ? result : "No Result");

            return result.isPresent() ? entityToDto(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
