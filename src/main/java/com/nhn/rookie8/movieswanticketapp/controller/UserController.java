package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.UserDTO;
import com.nhn.rookie8.movieswanticketapp.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    @Value("${accountURL}")
    private String accountUrl;


    @GetMapping("/register")
    public String register(){
        return "page/register_page";
    }

    @PostMapping("/register_process")
    public String registerProcess(HttpServletRequest request){
        String uid = request.getParameter("uid");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String url = request.getParameter("url");

        UserDTO userDTO = UserDTO.builder()
                .uid(uid)
                .password(password)
                .name(name)
                .email(email)
                .url(url)
                .build();

        log.info(userDTO.toString());
        RestTemplate template = new RestTemplate();
        UserResponseDTO userResponseDTO = template.postForObject(accountUrl+"/api/register",userDTO, UserResponseDTO.class);

        log.info(userResponseDTO);
        return "page/main_page";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "page/login_page";
    }


    @PostMapping("/login_process")
    public String loginProcess(HttpServletRequest request){

        String uid = request.getParameter("uid");
        String password = request.getParameter("password");

        UserDTO userDTO = UserDTO.builder()
                .uid(uid)
                .password(password)
                .build();

        RestTemplate template = new RestTemplate();
        log.info(accountUrl + "/api/login");
        UserResponseDTO userResponseDTO = template.postForObject(accountUrl+"/api/login", userDTO, UserResponseDTO.class);



        if(userResponseDTO == null || userResponseDTO.isError()){
            return "redirect:/user/login?err=1";
        }

        UserResponseDTO userInfo = template.postForObject(accountUrl+"/api/getUserInfo", userDTO, UserResponseDTO.class);

        if (userInfo == null) {
            return "redirect:/user/login?err=1";
        }

        HttpSession session = request.getSession();
        Map<String,String> content = (HashMap<String,String>) userInfo.getContent();
        session.setAttribute("uid", uid);
        session.setAttribute("name", content.get("name"));

        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/main";
    }

}
