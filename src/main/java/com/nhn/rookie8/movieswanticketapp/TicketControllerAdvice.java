package com.nhn.rookie8.movieswanticketapp;


import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.ticketexception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Log4j2
@ControllerAdvice
public class TicketControllerAdvice {

    @ExceptionHandler(UserNotFoundErrorException.class)
    public MemberDTO UserNotFoundErrorException(UserNotFoundErrorException e) {
        return MemberDTO.builder().build();
    }

    @ExceptionHandler(AlreadyKeyExistException.class)
    public String AlreadyKeyExistException(AlreadyKeyExistException e,
                                            RedirectAttributes redirectAttributes) {
        log.error(e);

        redirectAttributes.addFlashAttribute("message", "잘못된 로그인 요청입니다.");
        return "redirect:/member/login";
    }

    @ExceptionHandler(AlreadyExpiredOrNotExistKeyErrorException.class)
    public String AlreadyExpiredOrNotExistKeyErrorException(AlreadyExpiredOrNotExistKeyErrorException e,
                                                            RedirectAttributes redirectAttributes) {
        log.error(e);

        redirectAttributes.addFlashAttribute("member", MemberIdNameDTO.builder().build());
        return "redirect:/main";
    }

    @ExceptionHandler(SessionNotExistErrorException.class)
    public String SessionDoesNotExistErrorException(SessionNotExistErrorException e,
                                                    RedirectAttributes redirectAttributes) {
        log.error(e);

        redirectAttributes.addFlashAttribute("message", "로그인이 필요한 서비스입니다.");
        return "redirect:/member/login";
    }

    @ExceptionHandler(SessionExpiredErrorException.class)
    public String SessionExpiredErrorException(SessionExpiredErrorException e,
                                               RedirectAttributes redirectAttributes) {
        log.error(e);

        redirectAttributes.addFlashAttribute("message", "세션이 만료되었습니다. 다시 로그인해주십시오.");
        return "redirect:/member/login";
    }

    @ExceptionHandler(InvalidAuthKeyErrorException.class)
    public String InvalidAuthKeyErrorException(InvalidAuthKeyErrorException e,
                                               RedirectAttributes redirectAttributes) {
        log.error(e);

        redirectAttributes.addFlashAttribute("message", "잘못된 요청입니다. 다시 로그인해주십시오.");
        return "redirect:/member/login";
    }
}
