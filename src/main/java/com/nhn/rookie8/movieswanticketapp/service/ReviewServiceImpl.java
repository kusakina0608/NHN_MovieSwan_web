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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository repository;

    @Override
    public String registerReview(ReviewDTO reviewDTO) {
        String mid = reviewDTO.getMid();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rid").descending());
        BooleanBuilder booleanBuilder = getReviewsByMid(mid);

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
    public PageResultDTO<ReviewDTO, Review> getReviewPage(PageRequestDTO pageRequestDTO, String mid) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("rid").descending());
        BooleanBuilder booleanBuilder = getReviewsByMid(mid);

        Page<Review> result = repository.findAll(booleanBuilder, pageable);

        Function<Review, ReviewDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public ReviewDTO findMyReviewByMid(String mid, String uid) {
        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression1 = qReview.mid.eq(mid);
        booleanBuilder.and(expression1);
        BooleanExpression expression2 = qReview.uid.eq(uid);
        booleanBuilder.and(expression2);

        Optional<Review> result = repository.findAll(booleanBuilder, pageable).stream().findFirst();

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public PageResultDTO<ReviewDTO, Review> findMyReviews(PageRequestDTO pageRequestDTO, String uid) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("moddate").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.uid.eq(uid);
        booleanBuilder.and(expression);

        Page<Review> result = repository.findAll(booleanBuilder, pageable);

        Function<Review, ReviewDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public float getGradeByMid(String mid) {
        Pageable pageable = PageRequest.of(0, 1000);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.mid.eq(mid);
        booleanBuilder.and(expression);

        List<Review> result = repository.findAll(booleanBuilder, pageable).toList();
        if(result.isEmpty())
            return 0;
        else {
            float sum = 0;
            for (Review review : result)
                sum += review.getGrade();

            return sum / result.size();
        }
    }

    @Override
    public void editReview(ReviewDTO reviewDTO) {
        Optional<Review> result = repository.findById(reviewDTO.getRid());

        if(result.isPresent()) {
            Review review = result.get();

            review.changeGrade(reviewDTO.getGrade());
            review.changeContent(reviewDTO.getContent());

            repository.save(review);
        }
    }

    @Override
    public void deleteReview(String rid) {
        repository.deleteById(rid);
    }

    private BooleanBuilder getReviewsByMid(String mid) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.mid.eq(mid);
        booleanBuilder.and(expression);

        return booleanBuilder;
    }
}
