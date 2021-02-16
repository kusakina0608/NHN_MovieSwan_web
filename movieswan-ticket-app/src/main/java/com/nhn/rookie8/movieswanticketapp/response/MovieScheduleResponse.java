package com.nhn.rookie8.movieswanticketapp.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieScheduleResponse {
    private Boolean errorOccured;
    private List<Map<String, List<String>>> data;
}
