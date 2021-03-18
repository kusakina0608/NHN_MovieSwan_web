package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QReview;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.repository.ReviewRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        StringBuilder reservationId = new StringBuilder();
        if(lastReview.isPresent()) {
            String lastReviewId = lastReview.get().getReviewId();
            int num = Integer.parseInt(lastReviewId.substring(lastReviewId.lastIndexOf('-') + 1));
            reservationId.append(movieId).append("-").append(String.format("%05d", num + 1));
        }
        else
            reservationId.append(movieId).append("-00001");
        reviewDTO.setReviewId(reservationId.toString());

        Review review = dtoToEntity(reviewDTO);
        repository.save(review);

        return review.getReviewId();
    }

    @Override
    public PageResultDTO<ReviewDTO, Review> getReviewPage(PageRequestDTO pageRequestDTO, String movieId) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("reviewId").descending());
        BooleanBuilder booleanBuilder = getReviewsByMovieId(movieId);
        Page<Review> result = repository.findAll(booleanBuilder, pageable);

        return new PageResultDTO<>(result, this::entityToDTO);
    }

    @Override
    public ReviewDTO findMyReviewByMovieId(String movieId, String memberId) {
        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder booleanBuilder = myReviewByMovieIdBuilder(movieId, memberId);

        Optional<Review> result = repository.findAll(booleanBuilder, pageable).stream().findFirst();

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public PageResultDTO<ReviewDTO, Review> findMyReviews(PageRequestDTO pageRequestDTO, String memberId) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("modDate").descending());
        BooleanBuilder booleanBuilder = myReviewsBuilder(memberId);
        Page<Review> result = repository.findAll(booleanBuilder, pageable);

        return new PageResultDTO<>(result, this::entityToDTO);
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
        booleanBuilder.and(qReview.movieId.eq(movieId));
        return booleanBuilder;
    }

    private BooleanBuilder myReviewByMovieIdBuilder(String movieId, String memberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        booleanBuilder.and(qReview.movieId.eq(movieId));
        booleanBuilder.and(qReview.memberId.eq(memberId));
        return booleanBuilder;
    }

    private BooleanBuilder myReviewsBuilder(String memberId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QReview qReview = QReview.review;
        booleanBuilder.and(qReview.memberId.eq(memberId));
        return booleanBuilder;
    }
}
