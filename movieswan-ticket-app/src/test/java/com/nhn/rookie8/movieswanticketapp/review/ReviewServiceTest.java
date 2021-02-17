package com.nhn.rookie8.movieswanticketapp.review;

import com.nhn.rookie8.movieswanticketapp.dto.PageRequestDTO;
import com.nhn.rookie8.movieswanticketapp.dto.PageResultDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Review;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class ReviewServiceTest {
    @Autowired
    private ReviewService service;

    @Test
    public void reviewRegisterTest() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .mid("POKE001")
                    .uid("test" + i)
                    .grade(3)
                    .content("review" + i)
                    .build();

            service.register(reviewDTO);
        });
    }

    @Test
    public void reviewListTest() {
        PageRequestDTO requestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResultDTO<ReviewDTO, Review> resultDTO = service.getList(requestDTO);

        for(ReviewDTO review : resultDTO.getDtoList()) {
            System.out.println(review);
        }
    }

    @Test
    public void reviewModifyTest() {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rid("AAAA001-001")
                .mid("AAAA001")
                .uid("test01")
                .grade(5)
                .content("review1 - Modified")
                .build();

        service.modify(reviewDTO);
    }

    @Test
    public void reviewRemoveTest() {
        service.remove("AABB001-001");
    }

}
