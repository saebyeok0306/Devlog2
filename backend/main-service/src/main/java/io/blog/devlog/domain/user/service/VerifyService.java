package io.blog.devlog.domain.user.service;

public interface VerifyService {
    String createVerifyCode(String email);
    boolean checkVerifyCode(String email, String code);
    void removeExpiredCode();
    public void shutdown();
}
