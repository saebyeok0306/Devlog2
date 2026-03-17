package io.blog.devlog.domain.file.controller;

import io.blog.devlog.domain.file.model.File;
import io.blog.devlog.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/download")
public class DownloadController {
    @Value("${file.upload.path}")
    private String uploadPath;

    private final FileService fileService;

    @GetMapping("/{year}/{month}/{day}/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable int year,
                                             @PathVariable int month,
                                             @PathVariable int day,
                                             @PathVariable String fileName) throws IOException {
        String fileUrl = String.format("%04d/%02d/%02d/%s", year, month, day, fileName);
        String filePath = String.format("%s/%s", uploadPath, fileUrl);
        log.info("fileDownloadPath : {}", filePath);
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());

        File file = fileService.getFileByFileUrl(fileUrl);

        if (!resource.exists() || !resource.isReadable() || file == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
