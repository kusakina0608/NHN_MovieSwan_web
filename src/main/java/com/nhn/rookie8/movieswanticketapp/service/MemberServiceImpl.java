package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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

    //FIXME : memberDTO를 쓰면 id제외 모두 null 이므로 계정서버에서 올바르지 않은 요으로 간주함
    //FIXME : memberIdDTO를 써야함.
    @Override
    public MemberDTO getMemberInfoById(String memberId) {

        MemberResponseDTO memberInfo = template.postForObject(accountUrl + "/api/getMemberInfo",
                MemberIdDTO.builder().memberId(memberId).build(), MemberResponseDTO.class);
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
    public TokenResponseDTO loginService(MemberAuthDomainDTO memberAuthDomainDTO){
        HttpEntity<ExternalLoginDTO> entity =
                new HttpEntity<ExternalLoginDTO>(domainToExternalLoginDTO(memberAuthDomainDTO));

        String loginUrl = memberAuthDomainDTO.getIdDomain().equals("swan") ?
                accountUrl + "/api/auth" : externalLoginUrl.get(memberAuthDomainDTO.getIdDomain());

        try {
            ResponseEntity<TokenResponseDTO> responseEntity = template.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    entity,
                    TokenResponseDTO.class);

            return responseEntity.getBody();
        }
        catch (RestClientException e){
            return TokenResponseDTO.builder().build();
        }
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
