package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhn.rookie8.movieswanticketapp.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

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
        objectMapper.registerModule(new JavaTimeModule());
        template = new RestTemplate();
    }

    @Override
    public MemberDTO getMemberInfoById(String memberId) {
        MemberResponseDTO memberInfo = template.postForObject(accountUrl + "/api/getMemberInfo",
                MemberDTO.builder().memberId(memberId).build(), MemberResponseDTO.class);

        MemberDTO memberDTO = objectMapper.convertValue(memberInfo.getContent(), MemberDTO.class);

        return objectMapper.convertValue(memberInfo.getContent(), MemberDTO.class);
    }

    @Override
    public boolean checkResponse(MemberResponseDTO memberResponseDTO){
        return memberResponseDTO != null && !memberResponseDTO.isError();
    }

    @Override
    public MemberResponseDTO register(MemberRegisterDTO memberRegisterDTO){
        return template.postForObject(accountUrl+"/api/register", memberRegisterDTO, MemberResponseDTO.class);
    }

    @Override
    public MemberResponseDTO auth(MemberAuthDTO memberAuthDTO){
        return template.postForObject(accountUrl+"/api/auth", memberAuthDTO, MemberResponseDTO.class);
    }

    @Override
    public MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO){
        return objectMapper.convertValue(memberResponseDTO.getContent(), MemberIdNameDTO.class);
    }
}
