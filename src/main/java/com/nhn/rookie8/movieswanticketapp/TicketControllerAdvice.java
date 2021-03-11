package com.nhn.rookie8.movieswanticketapp;


import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.ticketexception.UserNotFoundErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Log4j2
@ControllerAdvice
@ResponseBody
public class TicketControllerAdvice {

    @ExceptionHandler(UserNotFoundErrorException.class)
    public MemberDTO UserNotFoundErrorException(UserNotFoundErrorException e) {
        return MemberDTO.builder().build();
    }
}
