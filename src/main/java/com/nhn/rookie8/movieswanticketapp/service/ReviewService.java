package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Review;

public interface ReviewService {
    String registerReview(ReviewDTO reviewDTO);

    PageResultDTO<ReviewDTO, Review> getReviewPage(PageRequestDTO pageRequestDTO, String mid);

    ReviewDTO findMyReviewByMid(String mid, String uid);

    PageResultDTO<ReviewDTO, Review> findMyReviews(PageRequestDTO pageRequestDTO, String uid);

    float getGradeByMid(String mid);

    void editReview(ReviewDTO reviewDTO);

    void deleteReview(String rid);

    default ReviewDTO entityToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .movieId(review.getMovieId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .reviewContent(review.getReviewContent())
                .regdate(review.getRegdate())
                .moddate(review.getModdate())
                .build();
    }

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .reviewId(reviewDTO.getReviewId())
                .movieId(reviewDTO.getMovieId())
                .userId(reviewDTO.getUserId())
                .rating(reviewDTO.getRating())
                .reviewContent(reviewDTO.getReviewContent())
                .build();
    }
}
