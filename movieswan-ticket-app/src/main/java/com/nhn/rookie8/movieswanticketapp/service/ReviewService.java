package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Review;

public interface ReviewService {
    String register(ReviewDTO reviewDTO);

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
                .regdate(reviewDTO.getRegdate())
                .moddate(reviewDTO.getModdate())
                .build();
        return review;
    }
}
