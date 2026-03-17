package io.blog.devlog.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class StartupConfigLogger implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();

        String url = env.getProperty("spring.datasource.url", "None");
        String username = env.getProperty("spring.datasource.username", "None");
        String password = env.getProperty("spring.datasource.password", "None");

        log.info("=====================================================");
        log.info("✅ DB Configuration Check");
        log.info("URL      : {}", maskString(url));
        log.info("Username : {}", maskString(username));
        log.info("Password : {}", maskString(password)); // 마스킹 처리
        log.info("=====================================================");
    }

    private String maskString(String str) {
        if (str == null || "None".equals(str)) {
            return "❌ [NOT SET]";
        }
        if (str.length() <= 3) return "***";
        return str.substring(0, 2) + "****" + str.substring(str.length() - 1);
    }
}
