package com.nhn.rookie8.movieswanticketapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.rookie8.movieswanticketapp.dto.TokenDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TokenRequestDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Auth;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@Log4j2
public class StorageService {
    @Value("${nhn.cloud.storageUrl}")
    private String storageUrl;
    @Value("${nhn.cloud.containerName}")
    private String containerName;
    @Value("${nhn.cloud.authUrl}")
    private String authUrl;
    @Value("${nhn.cloud.tenantId}")
    private String tenantId;
    @Value("${nhn.cloud.username}")
    private String username;
    @Value("${nhn.cloud.password}")
    private String password;
    private TokenRequestDTO tokenRequest;
    private TokenDTO token;
    private RestTemplate restTemplate;

    @PostConstruct
    public void setTokenRequest() {
        TokenRequestDTO.PasswordCredentials passwordCredentials = new TokenRequestDTO.PasswordCredentials(username, password);
        TokenRequestDTO.Auth auth = new TokenRequestDTO.Auth(tenantId, passwordCredentials);
        tokenRequest = new TokenRequestDTO(auth);
        restTemplate = new RestTemplate();
    }

    //토큰은 만료 됐는지 확인한후 만료 됐을 때만 새로 받기
    //objectmapper 바꾸기
    private TokenDTO requestToken(TokenRequestDTO tokenRequest) {
        String identityUrl = authUrl + "/tokens";

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<TokenRequestDTO> httpEntity = new HttpEntity<>(tokenRequest, headers);

        // 토큰 요청
        ResponseEntity<TokenDTO> response
                = this.restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, TokenDTO.class);
        log.info(response.getBody());
        return response.getBody();
    }

    private String getUrl(@NonNull String containerName, @NonNull String objectName) {
        return storageUrl + "/" + containerName + "/" + objectName;
    }

    public String uploadImage(MultipartFile file) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.warn("input stream 생성 실패", e);
            return "";
        }
        String imageName = createFileName(file);
        String url = this.getUrl(containerName, imageName);
        if(!isValidToken())
            token = requestToken(tokenRequest);

        // InputStream을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = new RequestCallback() {
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add("X-Auth-Token", token.getAccess().getToken().getId());
                IOUtils.copy(inputStream, request.getBody());
            }
        };

        // 오버라이드한 RequestCallback을 사용할 수 있도록 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<>(String.class, restTemplate.getMessageConverters());

        // API 호출
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return imageName;
    }

    public ResponseEntity<byte[]> displayImage(String objectName) {
        String url = this.getUrl(containerName, objectName);
        if(!isValidToken())
            token = requestToken(tokenRequest);
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", token.getAccess().getToken().getId());
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        // API 호출, 데이터를 바이트 배열로 받음
        return this.restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, byte[].class);
    }

    private String createFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String originalName = file.getOriginalFilename();
        String extensionName;
        try {
            if(originalName == null)
                throw new NullPointerException();
            extensionName = originalName.substring(originalName.lastIndexOf("."));
        } catch (NullPointerException e) {
            log.error("잘못된 파일 이름입니다.");
            return "";
        }

        return uuid + extensionName;
    }

    private boolean isValidToken() {
        return token != null || token.getAccess().getToken().getExpires().isAfter(LocalDateTime.now());
    }
}
