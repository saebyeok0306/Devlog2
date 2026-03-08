package io.blog.devlog.domain.file.controller;

import io.blog.devlog.domain.file.dto.FileDto;
import io.blog.devlog.domain.file.handler.FileHandler;
import io.blog.devlog.domain.file.model.File;
import io.blog.devlog.domain.file.service.FileService;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.global.exception.NoPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getPrincipalRole;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
@Slf4j
public class FileController {
    private final FileHandler fileHandler;
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileDto> uploadFiles(@RequestParam("file") MultipartFile file) {
        /* 여기서는 TempFile 테이블에만 올리고, 가공한 FileDto 데이터를 Response함. 업로드 단계에서 TempFile을 삭제하고 File로 Upload */
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Role role = getPrincipalRole();
        if (role == null || role.equals(Role.GUEST)) {
            throw new NoPermissionException("파일 업로드 권한이 없습니다.");
        }
        log.info("File uploaded : " + file.getOriginalFilename());
        log.info("File type : " + file.getContentType());
        FileDto fileDto = fileHandler.uploadFile(file);
        return ResponseEntity.ok(fileDto);
    }

    @GetMapping("/post/{entityId}")
    public ResponseEntity<List<FileDto>> getPostFiles(@PathVariable Long entityId) {
        List<File> postFiles = fileService.getFilesByPostId(entityId);
        return ResponseEntity.ok(postFiles.stream().map(FileDto::of).toList());
    }

    @GetMapping("/comment/{entityId}")
    public ResponseEntity<List<FileDto>> getCommentFiles(@PathVariable Long entityId) {
        List<File> postFiles = fileService.getFilesByCommentId(entityId);
        return ResponseEntity.ok(postFiles.stream().map(FileDto::of).toList());
    }
}
