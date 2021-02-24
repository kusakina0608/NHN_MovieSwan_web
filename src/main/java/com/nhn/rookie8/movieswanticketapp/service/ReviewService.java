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

    default ReviewDTO entityToDTO(Review review) {
        return ReviewDTO.builder()
                .rid(review.getRid())
                .mid(review.getMid())
                .uid(review.getUid())
                .grade(review.getGrade())
                .content(review.getContent())
                .regdate(review.getRegdate())
                .moddate(review.getModdate())
                .build();
    }

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .rid(reviewDTO.getRid())
                .mid(reviewDTO.getMid())
                .uid(reviewDTO.getUid())
                .grade(reviewDTO.getGrade())
                .content(reviewDTO.getContent())
                .build();
    }
}
