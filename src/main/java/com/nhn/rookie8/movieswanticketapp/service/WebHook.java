package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;

public interface WebHook {
    void sendReservationSuccessMessage(MemberDTO memberDTO, MovieDTO movieDTO, ReservationDTO reservationDTO);

    void sendReservationCanceledMessage(MemberDTO memberDTO, String movieTitle);
}
