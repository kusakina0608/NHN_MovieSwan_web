package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    ObjectMapper objectMapper;

    RestTemplate template;

    @Override
    public MemberDTO getMemberInfoById(String memberId) {
        try {
            MemberDTO requestDTO = MemberDTO.builder()
                    .memberId(memberId)
                    .build();

            RestTemplate template = new RestTemplate();
            MemberResponseDTO memberInfo = template.postForObject(accountUrl + "/api/getMemberInfo", requestDTO, MemberResponseDTO.class);

            if (memberInfo == null)
                throw new NullPointerException();

            Map<String, String> content = (HashMap<String, String>) memberInfo.getContent();

            return MemberDTO.builder()
                    .memberId(content.get("memberId"))
                    .name(content.get("name"))
                    .email(content.get("email"))
                    .url(content.get("url"))
                    .regDate(LocalDateTime.parse(content.get("regDate")))
                    .modDate(LocalDateTime.parse(content.get("modDate")))
                    .build();
        } catch (Exception e) {
            log.error(e);
            return new MemberDTO();
        }
    }

    @Override
    public boolean checkResponse(MemberResponseDTO response){
        return response != null && !response.isError();
    }

    @Override
    public MemberResponseDTO register(Map<String, String[]> requestMap){

        objectMapper = new ObjectMapper();
        template = new RestTemplate();

        MemberRegisterDTO memberRegisterDTO = objectMapper.convertValue(requestMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0])), MemberRegisterDTO.class);

        return template.postForObject(accountUrl+"/api/register", memberRegisterDTO, MemberResponseDTO.class);

    }

    @Override
    public MemberResponseDTO auth(Map<String, String[]> requestMap){

        objectMapper = new ObjectMapper();
        template = new RestTemplate();

        MemberAuthDTO memberAuthDTO = objectMapper.convertValue(requestMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0])), MemberAuthDTO.class);

        return template.postForObject(accountUrl+"/api/auth", memberAuthDTO, MemberResponseDTO.class);

    }

    @Override
    public MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO){
        objectMapper = new ObjectMapper();

        return objectMapper.convertValue(memberResponseDTO.getContent(), MemberIdNameDTO.class);
    }
}
