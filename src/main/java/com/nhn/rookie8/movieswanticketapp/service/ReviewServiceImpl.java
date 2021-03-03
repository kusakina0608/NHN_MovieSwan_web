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
        String movieId = reviewDTO.getMovieId();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewId").descending());
        BooleanBuilder booleanBuilder = getReviewsByMovieId(movieId);

        Optional<Review> lastReview = repository.findAll(booleanBuilder, pageable).stream().findFirst();
        String reservationId;
        if(lastReview.isPresent()) {
            String lastReviewId = lastReview.get().getReviewId();
            int num = Integer.parseInt(lastReviewId.substring(lastReviewId.lastIndexOf('-') + 1));
            reservationId = movieId + "-" + String.format("%05d", num + 1);
        }
        else
            reservationId = movieId + "-00001";
        reviewDTO.setReviewId(reservationId);

        Review review = dtoToEntity(reviewDTO);

        repository.save(review);

        return review.getReviewId();
    }

    @Override
    public PageResultDTO<ReviewDTO, Review> getReviewPage(PageRequestDTO pageRequestDTO, String movieId) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("reviewId").descending());
        BooleanBuilder booleanBuilder = getReviewsByMovieId(movieId);

        Page<Review> result = repository.findAll(booleanBuilder, pageable);

        Function<Review, ReviewDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public ReviewDTO findMyReviewByMovieId(String movieId, String memberId) {
        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression1 = qReview.movieId.eq(movieId);
        booleanBuilder.and(expression1);
        BooleanExpression expression2 = qReview.memberId.eq(memberId);
        booleanBuilder.and(expression2);

        Optional<Review> result = repository.findAll(booleanBuilder, pageable).stream().findFirst();

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public PageResultDTO<ReviewDTO, Review> findMyReviews(PageRequestDTO pageRequestDTO, String memberId) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("modDate").descending());
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.memberId.eq(memberId);
        booleanBuilder.and(expression);

        Page<Review> result = repository.findAll(booleanBuilder, pageable);

        Function<Review, ReviewDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public float getRatingByMovieId(String movieId) {
        Pageable pageable = PageRequest.of(0, 1000);
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.movieId.eq(movieId);
        booleanBuilder.and(expression);

        List<Review> result = repository.findAll(booleanBuilder, pageable).toList();
        if(result.isEmpty())
            return 0;
        else {
            float sum = 0;
            for (Review review : result)
                sum += review.getRating();

            return sum / result.size();
        }
    }

    @Override
    public void editReview(ReviewDTO reviewDTO) {
        Optional<Review> result = repository.findById(reviewDTO.getReviewId());

        if(result.isPresent()) {
            Review review = result.get();

            review.changeRating(reviewDTO.getRating());
            review.changeContent(reviewDTO.getContent());

            repository.save(review);
        }
    }

    @Override
    public void deleteReview(String reviewId) {
        repository.deleteById(reviewId);
    }

    private BooleanBuilder getReviewsByMovieId(String movieId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        BooleanExpression expression = qReview.movieId.eq(movieId);
        booleanBuilder.and(expression);

        return booleanBuilder;
    }
}
