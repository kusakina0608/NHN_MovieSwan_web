package com.nhn.rookie8.movieswanticketapp.controller;

import com.nhn.rookie8.movieswanticketapp.dto.UserDTO;
import com.nhn.rookie8.movieswanticketapp.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Value("${accountURL}")
    private String url;


    @GetMapping("/register")
    public String register(){
        return null;
    }

    @PostMapping("/register_process")
    public String register_process(){
        return null;
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

        UserResponseDTO userResponseDTO = template.postForObject(url+"/api/login", userDTO, UserResponseDTO.class);
        UserResponseDTO userInfo = template.postForObject(url+"/api/getUserInfo", userDTO, UserResponseDTO.class);


        if(userResponseDTO.isError()){
            return "redirect:/user/login?err=1";
        }

        HttpSession session = request.getSession();
        String name = userInfo.getContent().getName();

        session.setAttribute("uid", uid);
        session.setAttribute("name", name);

        return "redirect:/main";
    }

}
