package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.UserDTO;
import com.nhn.rookie8.movieswanticketapp.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService{

    @Value("${accountURL}")
    private String accountUrl;

    @Override
    public UserDTO getUserInfoById(String uid) {
        try {
            UserDTO requestDTO = UserDTO.builder()
                    .uid(uid)
                    .build();

            RestTemplate template = new RestTemplate();
            UserResponseDTO userInfo = template.postForObject(accountUrl + "/api/getUserInfo", requestDTO, UserResponseDTO.class);

            if (userInfo == null)
                throw new NullPointerException();

            Map<String, String> content = (HashMap<String, String>) userInfo.getContent();

            UserDTO result = UserDTO.builder()
                    .uid(content.get("uid"))
                    .name(content.get("name"))
                    .email(content.get("email"))
                    .url(content.get("url"))
                    .regDate(LocalDateTime.parse(content.get("regDate")))
                    .modDate(LocalDateTime.parse(content.get("modDate")))
                    .build();

            return result;
        } catch (Exception e) {
            log.error(e);
            return new UserDTO();
        }
    }
}
