package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Review;

public interface ReviewService {
    String register(ReviewDTO reviewDTO);

    PageResultDTO<ReviewDTO, Review> getList(PageRequestDTO pageRequestDTO, String mid);

    ReviewDTO findMyReviewByMid(String mid, String uid);

    PageResultDTO<ReviewDTO, Review> findMyReviews(PageRequestDTO pageRequestDTO, String uid);

    float getGradeByMid(String mid);

    void modify(ReviewDTO reviewDTO);

    void remove(String rid);

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .reviewId(reviewDTO.getReviewId())
                .movieId(reviewDTO.getMovieId())
                .memberId(reviewDTO.getMemberId())
                .rating(reviewDTO.getRating())
                .content(reviewDTO.getContent())
                .build();
    }

    default ReviewDTO entityToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .movieId(review.getMovieId())
                .memberId(review.getMemberId())
                .rating(review.getRating())
                .content(review.getContent())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();
    }
}
