package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MovieDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ReservationDetailDTO;

public interface WebHook {
    public static final String botName = "Movie Swan";

    public static final String botIconImage = "https://daejin-lee.github.io/assets/profile.png";

    void sendReservationSuccessMessage(MemberDTO memberDTO, MovieDTO movieDTO, ReservationDTO reservationDTO);

    void sendReservationCanceledMessage(MemberDTO memberDTO, ReservationDetailDTO reservationDetailDTO);
}
