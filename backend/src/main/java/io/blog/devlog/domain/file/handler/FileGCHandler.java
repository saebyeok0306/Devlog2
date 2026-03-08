package io.blog.devlog.domain.file.handler;

import io.blog.devlog.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileGCHandler {
    /* TODO: 삭제기준
    * 1. tempFile은 업로드시간 기준 24시간 이후 삭제
    * 2. Entity 연결이 끊긴 File
    *  */
    private final FileService fileService;

    @Scheduled(cron = "0 30 */2 * * ?")
    public void TempFileGarbageCollector() {
        log.info("TempFile Garbage Collector Start");
        fileService.deleteTempFilesByHour(24);
    }
}
