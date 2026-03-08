package io.blog.devlog.domain.captcha.service;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import io.blog.devlog.domain.captcha.dto.ResponseCaptchaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {
    @Value("${recaptcha.verify_url}")
    private String url;
    @Value("${recaptcha.secret_key}")
    private String key;
    private static final double HALF = 0.5;
    @Override
    public ResponseCaptchaDto verifyToken(String token) {
        try {
            if (token.isEmpty()) {
                throw new BadRequestException("Captcha verification failed: token is empty");
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("secret", key);
            map.add("response", token);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class); // 해당 url로 token과 secret key를 전송. 유효성 검증.

            if (response.getBody() == null) {
                throw new BadRequestException("Captcha verification failed: response body is null");
            }

            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            log.info("Captcha verification response: {}", jsonObject.toString());
            boolean success = jsonObject.get("success").getAsBoolean();
            return ResponseCaptchaDto.builder()
                    .success(success)
                    .build();
        } catch (Exception e) {
            log.error("Captcha verification failed: " + e.getMessage());
        }
        return ResponseCaptchaDto.builder()
                .success(false)
                .build();
    }
}
