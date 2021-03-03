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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate template = new RestTemplate();

    @GetMapping("/register")
    public String register(){
        return "page/register_page";
    }

    @PostMapping("/register_process")
    public String registerProcess(HttpServletRequest request){

        MemberRegisterDTO memberRegisterDTO = objectMapper.convertValue(request.getParameterMap(), MemberRegisterDTO.class);

        MemberResponseDTO memberResponseDTO = template.postForObject(accountUrl+"/api/register", memberRegisterDTO, MemberResponseDTO.class);

        log.info(memberResponseDTO);
        return "page/main_page";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "page/login_page";
    }


    @PostMapping("/login_process")
    public String loginProcess(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){

        //request.getParameterMap() : Map<String,String[]>

        MemberAuthDTO memberAuthDTO = objectMapper.convertValue(request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0])), MemberAuthDTO.class);

        MemberResponseDTO memberResponseDTO = template.postForObject(accountUrl+"/api/auth", memberAuthDTO, MemberResponseDTO.class);

        if(!memberService.checkResponse(memberResponseDTO)){
            redirectAttributes.addFlashAttribute("message","ID 또는 Password가 잘못 입력 되었습니다.");
            return "redirect:/member/login";
        }

        MemberIdNameDTO memberIdNameDTO = objectMapper.convertValue(memberResponseDTO.getContent(), MemberIdNameDTO.class);


        HttpSession session = request.getSession();
        session.setAttribute("member", objectMapper.convertValue(memberIdNameDTO, new TypeReference<Map<String,String>>() {}));

        redirectAttributes.addFlashAttribute("member", session.getAttribute("member"));
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        HttpSession session = httpServletRequest.getSession(false);
        if(session != null) {
            session.invalidate();
        }

        redirectAttributes.addFlashAttribute("member", MemberIdNameDTO.builder().build());
        return "redirect:/main";
    }

}
