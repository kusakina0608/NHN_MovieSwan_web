package com.nhn.rookie8.movieswanticketapp.dto;

import lombok.*;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {
    private double discountRatio = 0.0;
    private String discountType = "없음";

    public DiscountDTO(int hour) {
        if(hour < 9){
            discountRatio = 0.25;
            discountType = "조조 할인(오전 09:00 이전)";
        }
        else{
            discountRatio = 0.0;
            discountType = "없음";
        }
    }
}
