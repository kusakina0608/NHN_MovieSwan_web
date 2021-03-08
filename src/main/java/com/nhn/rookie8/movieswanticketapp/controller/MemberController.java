package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.MemberIdNameDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberResponseDTO;
import com.nhn.rookie8.movieswanticketapp.service.AuthService;
import com.nhn.rookie8.movieswanticketapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    private final MemberService memberService;

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String register(){
        return "page/register_page";
    }

    @PostMapping("/register_process")
    public String registerProcess(HttpServletRequest request){

        memberService.register(request.getParameterMap());

        return "page/main_page";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "page/login_page";
    }


    @PostMapping("/login_process")
    public String loginProcess(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes){

        MemberResponseDTO memberResponseDTO = memberService.auth(request.getParameterMap());

        if(!memberService.checkResponse(memberResponseDTO)){
            redirectAttributes.addFlashAttribute("message","ID 또는 Password가 잘못 입력 되었습니다.");
            return "redirect:/member/login";
        }

        String cookieValue = RandomStringUtils.randomAlphanumeric(32);
        Cookie cookie = new Cookie("SWANAUTH", cookieValue);
        cookie.setMaxAge(-1);
        cookie.setPath("/");

        response.addCookie(cookie);
        authService.saveMemberInfo(cookieValue, memberService.responseToMemberIdNameMap(memberResponseDTO));

        redirectAttributes.addFlashAttribute("member", authService.readMemberInfo(cookieValue));
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Cookie[] cookies = request.getCookies();
        String authKey = new String();

        if (cookies != null)
            for (int i = 0; i < cookies.length; i++)
                if (cookies[i].getName().equals("SWANAUTH")) {
                    authKey = cookies[i].getValue();
                    break;
                }

        if (authService.validMemberInfo(authKey))
            authService.expireAuth(authKey);

        redirectAttributes.addFlashAttribute("member", MemberIdNameDTO.builder().build());
        return "redirect:/main";
    }
}
