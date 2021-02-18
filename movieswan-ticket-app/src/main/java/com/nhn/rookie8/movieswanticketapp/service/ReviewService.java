package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Review;

import java.util.Optional;

public interface ReviewService {
    String register(ReviewDTO reviewDTO);

    PageResultDTO<ReviewDTO, Review> getList(PageRequestDTO pageRequestDTO, String mid);

    Optional<Review> findMyReview(String mid, String uid);

    float getGradeByMid(String mid);

    void modify(ReviewDTO reviewDTO);

    void remove(String rid);

    default ReviewDTO entityToDTO(Review review) {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rid(review.getRid())
                .mid(review.getMid())
                .uid(review.getUid())
                .grade(review.getGrade())
                .content(review.getContent())
                .regdate(review.getRegdate())
                .moddate(review.getModdate())
                .build();
        return reviewDTO;
    }

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        Review review = Review.builder()
                .rid(reviewDTO.getRid())
                .mid(reviewDTO.getMid())
                .uid(reviewDTO.getUid())
                .grade(reviewDTO.getGrade())
                .content(reviewDTO.getContent())
                .build();
        return review;
    }
}
