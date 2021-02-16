package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.QuestionDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Question;
import com.nhn.rookie8.movieswanticketapp.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repository;

    @Override
    public void registerQuestion(QuestionDTO dto) {
        Question entity = dtoToEntity(dto);
        repository.save(entity);
    }

    @Override
    public QuestionDTO readQuestion(Integer qid) {
        Optional<Question> result = repository.findById(qid);
        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public PageResultDTO<QuestionDTO, Question> getQuestionList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("qid").descending());

        Page<Question> result = repository.findAll(pageable);
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
