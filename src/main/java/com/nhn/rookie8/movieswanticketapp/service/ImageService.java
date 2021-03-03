package com.nhn.rookie8.movieswanticketapp.service;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Data
@Log4j2
public class ImageService {
    private String tokenId;
    private String storageUrl;
    private RestTemplate restTemplate;

    public ImageService(String storageUrl, String tokenId) {
        this.setStorageUrl(storageUrl);
        this.setTokenId(tokenId);

        this.restTemplate = new RestTemplate();
    }

    private String getUrl(@NonNull String containerName, @NonNull String objectName) {
        return this.getStorageUrl() + "/" + containerName + "/" + objectName;
    }

    public String uploadImage(String containerName, MultipartFile file) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.warn("input stream 생성 실패", e);
            return "";
        }
        String imageName = createFileName(file);
        String url = this.getUrl(containerName, imageName);

        // InputStream을 요청 본문에 추가할 수 있도록 RequestCallback 오버라이드
        final RequestCallback requestCallback = new RequestCallback() {
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add("X-Auth-Token", tokenId);
                IOUtils.copy(inputStream, request.getBody());
            }
        };

        // 오버라이드한 RequestCallback을 사용할 수 있도록 설정
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<String>(String.class, restTemplate.getMessageConverters());

        // API 호출
        restTemplate.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        return imageName;
    }

    public ResponseEntity<byte[]> displayImage(String containerName, String objectName) {
        String url = this.getUrl(containerName, objectName);

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", tokenId);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> requestHttpEntity = new HttpEntity<String>(null, headers);

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
}
