package com.nhn.rookie8.movieswanticketapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @Value("${accountURL}")
    private String accountUrl;

    @Autowired
    private final MemberService memberService;


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
    public String loginProcess(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){

        MemberResponseDTO memberResponseDTO = memberService.auth(request.getParameterMap());

        if(!memberService.checkResponse(memberResponseDTO)){
            redirectAttributes.addFlashAttribute("message","ID 또는 Password가 잘못 입력 되었습니다.");
            return "redirect:/member/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("member", memberService.responseToMemberIdNameMap(memberResponseDTO));
        redirectAttributes.addFlashAttribute("member", session.getAttribute("member"));

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        if(session != null) {session.invalidate();}
        redirectAttributes.addFlashAttribute("member", MemberIdNameDTO.builder().build());
        return "redirect:/main";
    }

}
