package com.nhn.rookie8.movieswanticketapp.review;

import com.nhn.rookie8.movieswanticketapp.dto.ReviewDTO;
import com.nhn.rookie8.movieswanticketapp.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewServiceTest {
    @Autowired
    private ReviewService service;

    @Test
    public void reviewRegisterTest() {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rid("AAAA001-001")
                .mid("AAAA001")
                .uid("test01")
                .grade(3)
                .content("review1")
                .build();

        String rid = service.register(reviewDTO);
        System.out.println(rid);
    }

}
