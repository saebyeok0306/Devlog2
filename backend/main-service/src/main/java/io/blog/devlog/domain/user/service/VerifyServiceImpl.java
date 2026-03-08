package io.blog.devlog.domain.user.service;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerifyServiceImpl implements VerifyService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final HashMap<String, VerifyCode> verifyCodeMap = new HashMap<>();
    private final long EXPIRE_TIME = 1000 * 60 * 5;


    public VerifyServiceImpl() {
        // 5분마다 만료된 코드를 제거하는 스케줄러를 실행합니다.
        log.info("VerifyService started");
        scheduler.scheduleAtFixedRate(this::removeExpiredCode, EXPIRE_TIME, EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    private static class VerifyCode {
        @Getter
        private final String code;
        private final long expireAt;

        public VerifyCode(String code, long expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }

    @Override
    public String createVerifyCode(String email) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    sb.append((char) ((int) (random.nextInt(26)) + 97));
                    break;
                case 1:
                    sb.append((char) ((int) (random.nextInt(26)) + 65));
                    break;
                default:
                    sb.append(random.nextInt(10));
                    break;
            }
        }

        String code = sb.toString();
        verifyCodeMap.put(email, new VerifyCode(code, EXPIRE_TIME + System.currentTimeMillis()));
        return code;
    }

    @Override
    public boolean checkVerifyCode(String email, String code) {
        VerifyCode verifyCode = verifyCodeMap.get(email);
        if (verifyCode == null) return false;
        if (verifyCode.isExpired()) {
            verifyCodeMap.remove(email);
            return false;
        }
        if (code.equals(verifyCode.getCode())) {
            verifyCodeMap.remove(email);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeExpiredCode() {
        // 모든 HashMap을 순회하여 만료된 코드를 제거합니다.
        verifyCodeMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    @PreDestroy
    @Override
    public void shutdown() {
        log.info("VerifyService shutdown");
        scheduler.shutdown();
    }
}
