package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.*;
import com.nhn.rookie8.movieswanticketapp.ticketexception.UserNotFoundErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    @Value("${accountURL}")
    private String accountUrl;

    private ObjectMapper objectMapper;
    private RestTemplate template;

    @PostConstruct
    public void initialize(){
        objectMapper = new ObjectMapper();
        template = new RestTemplate();
    }

    @Override
    public MemberDTO getMemberInfoById(String memberId) {
        MemberResponseDTO memberInfo = template.postForObject(accountUrl + "/api/getMemberInfo",
                MemberDTO.builder().memberId(memberId).build(), MemberResponseDTO.class);

        return objectMapper.convertValue(memberInfo.getContent(), MemberDTO.class);
    }

    @Override
    public boolean checkResponse(MemberResponseDTO response){
        return response != null && !response.isError();
    }

    @Override
    public MemberResponseDTO register(MemberRegisterDTO request){
        return template.postForObject(accountUrl+"/api/register", request, MemberResponseDTO.class);
    }

    @Override
    public MemberResponseDTO auth(MemberAuthDTO request){
        return template.postForObject(accountUrl+"/api/auth", request, MemberResponseDTO.class);
    }

    @Override
    public MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO){
        return objectMapper.convertValue(memberResponseDTO.getContent(), MemberIdNameDTO.class);
    }
}
