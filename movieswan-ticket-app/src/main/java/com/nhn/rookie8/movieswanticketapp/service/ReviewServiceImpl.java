package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QReview;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.repository.ReviewRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository repository;

    @Override
    public String register(ReviewDTO reviewDTO) {
        String mid = reviewDTO.getMid();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rid").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.mid.eq(mid);
        booleanBuilder.and(expression);

        Optional<Review> lastReview = repository.findAll(booleanBuilder, pageable).stream().findFirst();
        String rid;
        if(lastReview.isPresent()) {
            String lastRid = lastReview.get().getRid();
            int num = Integer.parseInt(lastRid.substring(lastRid.lastIndexOf('-') + 1));
            rid = mid + "-" + String.format("%05d", num + 1);
        }
        else
            rid = mid + "-00001";
        reviewDTO.setRid(rid);

        Review review = dtoToEntity(reviewDTO);

        repository.save(review);

        return review.getRid();
    }

    @Override
    public PageResultDTO<ReviewDTO, Review> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("mid").descending());

        Page<Review> result = repository.findAll(pageable);

        Function<Review, ReviewDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {
        Optional<Review> result = repository.findById(reviewDTO.getRid());

        if(result.isPresent()) {
            Review review = result.get();

            review.changeGrade(reviewDTO.getGrade());
            review.changeContent(reviewDTO.getContent());

            repository.save(review);
        }
    }

    @Override
    public void remove(String rid) {
        repository.deleteById(rid);
    }
}
