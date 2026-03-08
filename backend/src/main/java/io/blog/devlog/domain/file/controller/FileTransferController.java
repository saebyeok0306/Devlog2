package io.blog.devlog.domain.file.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/res")
@Profile({"local", "dev"})
public class FileTransferController {
    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/{year}/{month}/{day}/{fileName:.+}")
    public ResponseEntity<Resource> getImage( @PathVariable int year,
                                              @PathVariable int month,
                                              @PathVariable int day,
                                              @PathVariable String fileName) throws IOException {
        String imagePath = String.format("%s/%04d/%02d/%02d/%s", uploadPath, year, month, day, fileName);
        Path path = Paths.get(imagePath);
        Resource resource = new UrlResource(path.toUri());

        MediaType mediaType = determineMediaType(path);

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(mediaType)
                .body(resource);
    }

    private MediaType determineMediaType(Path path) throws IOException {
        String contentType;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new IOException("파일 타입을 확인할 수 없습니다: " + path, e);
        }

        return MediaType.parseMediaType(contentType);
    }
}
