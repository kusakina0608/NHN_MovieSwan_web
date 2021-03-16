package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MemberAuthDomainDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberRegisterDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberResponseDTO;
import com.nhn.rookie8.movieswanticketapp.service.AuthService;
import com.nhn.rookie8.movieswanticketapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @Value("${accountURL}")
    private String accountUrl;

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/register")
    public String register(){
        return "page/register_page";
    }

    @PostMapping("/register_process")
    public String registerProcess(@ModelAttribute MemberRegisterDTO request){

        memberService.register(request);

        return "page/main_page";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "page/login_page";
    }

    @PostMapping("/login_service")
    public String tokenProcess(@ModelAttribute MemberAuthDomainDTO memberAuthDomainDTO,
                               RedirectAttributes redirectAttributes) {
        String url = memberService.loginService(memberAuthDomainDTO).getUrl();

        if (url == null) {
            redirectAttributes.addFlashAttribute("message", "ID 또는 Password가 잘못 입력 되었습니다.");
            return "redirect:/member/login";
        }
        return "redirect:" + url;
    }

    @GetMapping("/login_process")
    public String loginProcess(@RequestParam String token,
                               HttpServletResponse response, RedirectAttributes redirectAttributes){
        MemberResponseDTO memberResponseDTO = memberService.verifyToken(token);

        if(!memberService.checkResponse(memberResponseDTO)){
            redirectAttributes.addFlashAttribute("message","ID 또는 Password가 잘못 입력 되었습니다.");
            return "redirect:/member/login";
        }

        Cookie cookie = authService.setSession(memberService.responseToMemberIdNameMap(memberResponseDTO));

        response.addCookie(cookie);
        redirectAttributes.addFlashAttribute("member", authService.getMemberInfo(cookie.getValue()));
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        authService.expireSession(authService.getAuthKey(request.getCookies()));

        redirectAttributes.addFlashAttribute("member", MemberIdNameDTO.builder().build());
        return "redirect:/main";
    }
}
