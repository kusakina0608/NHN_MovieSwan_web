package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    @Value("${accountURL}")
    private String accountUrl;

    @Value("#{${external.login.url}}")
    private Map<String, String> externalLoginUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate template;

    @Override
    public MemberDTO getMemberInfoById(String memberId) {
        MemberResponseDTO memberInfo = template.postForObject(accountUrl + "/api/getMemberInfo",
                MemberDTO.builder().memberId(memberId).build(), MemberResponseDTO.class);

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
    public TokenResponseDTO loginService(MemberAuthDomainDTO memberAuthDomainDTO) {
        HttpEntity<ExternalLoginDTO> entity =
                new HttpEntity<ExternalLoginDTO>(domainToExternalLoginDTO(memberAuthDomainDTO));

        //TODO : Status Code 에 따른 분기 처리
        ResponseEntity<TokenResponseDTO> responseEntity = template.exchange(
                externalLoginUrl.get(memberAuthDomainDTO.getIdDomain()),
                HttpMethod.POST,
                entity,
                TokenResponseDTO.class);

        return responseEntity.getBody();
    }

    @Override
    public MemberResponseDTO verifyToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return template.exchange(
                accountUrl + "/api/verifyToken",
                HttpMethod.GET,
                entity,
                MemberResponseDTO.class
        ).getBody();
    }

    @Override
    public MemberIdNameDTO responseToMemberIdNameMap(MemberResponseDTO memberResponseDTO){
        return objectMapper.convertValue(memberResponseDTO.getContent(), MemberIdNameDTO.class);
    }

    @Override
    public ExternalLoginDTO domainToExternalLoginDTO(MemberAuthDomainDTO memberAuthDomainDTO) {
        return ExternalLoginDTO.builder()
                .id(memberAuthDomainDTO.getMemberId())
                .password(memberAuthDomainDTO.getPassword())
                .build();
    }
}
