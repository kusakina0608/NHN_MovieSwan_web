package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.*;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {
    private double discountRatio = 0.0;
    private String discountType = "없음";
}
