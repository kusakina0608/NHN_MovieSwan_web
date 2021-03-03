package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.MemberDTO;
import com.nhn.rookie8.movieswanticketapp.dto.MemberResponseDTO;
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
public class MemberServiceImpl implements MemberService {

    @Value("${accountURL}")
    private String accountUrl;

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
}
