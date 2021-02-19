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
        return "page/register_page";
    }

    @PostMapping("/register_process")
    public String register_process(HttpServletRequest request){
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

        System.out.println(userDTO.toString());
        RestTemplate template = new RestTemplate();
        UserResponseDTO userResponseDTO = template.postForObject(url+"/api/register",userDTO, UserResponseDTO.class);

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

        UserResponseDTO userResponseDTO = template.postForObject(url+"/api/login",userDTO, UserResponseDTO.class);


        if(userResponseDTO.isError()){
            return "redirect:/user/login?err=1";
        }

        HttpSession session = request.getSession();
        session.setAttribute("uid",uid);
        return "page/main_page";
    }

}
