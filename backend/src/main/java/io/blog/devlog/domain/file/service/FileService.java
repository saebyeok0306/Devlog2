package io.blog.devlog.domain.file.service;

import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.file.dto.FileDto;
import io.blog.devlog.domain.file.handler.FileHandler;
import io.blog.devlog.domain.file.model.EntityType;
import io.blog.devlog.domain.file.model.File;
import io.blog.devlog.domain.file.model.TempFile;
import io.blog.devlog.domain.file.repository.FileRepository;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final TempFileService tempFileService;
    private final FileHandler fileHandler;

    public File addFile(File file) {
        return fileRepository.save(file);
    }

    public <T> void uploadFileAndDeleteTempFile(T original, List<FileDto> uploadFiles) {
        for (FileDto uploadFile : uploadFiles) {
            try {
                if (uploadFile.isExist()) {
                    continue;
                }
                System.out.println("uploadFile = " + uploadFile.toString());
                tempFileService.deleteTempFile(uploadFile.getTempId());

                switch (original.getClass().getSimpleName()) {
                    case "Post":
                        this.addFile(uploadFile.toEntity((Post) original));
                        break;
                    case "Comment":
                        this.addFile(uploadFile.toEntity((Comment) original));
                        break;
                    case "User":
                        this.addFile(uploadFile.toEntity((User) original));
                        break;
                    default:
                        throw new RuntimeException("Unknown entity type" + original.getClass().getSimpleName());
                }
            } catch (Exception e) {
                log.error("Temp file not found : " + uploadFile.getTempId());
            }
        }
    }

    public File getFileByFileUrl(String fileUrl) {
        return fileRepository.findByFileUrl(fileUrl).orElse(null);
    }

    public List<File> getFilesByPostId(Long postId) {
        return fileRepository.findByEntityTypeAndEntityId(EntityType.POST, postId);
    }

    public List<File> getFilesByCommentId(Long commentId) {
        return fileRepository.findByEntityTypeAndEntityId(EntityType.COMMENT, commentId);
    }

    public Optional<File> getFileByUserId(Long userId) {
        List<File> files = fileRepository.findByEntityTypeAndEntityId(EntityType.USER, userId);
        if (files.isEmpty()) return Optional.empty();
        return Optional.of(files.get(0));
    }

    public void deleteFileFromComment(Comment comment) {
        List<File> files = this.getFilesByCommentId(comment.getId());
        if (files.isEmpty()) return;
        for (File file : files) {
            try {
                fileHandler.deleteFile(file.getFileUrl());
                fileRepository.delete(file);
            } catch (IOException e) {
                log.error("File not found : " + file.getFileUrl());
            }
        }
    }

    public void deleteFileFromPost(Post post) {
        List<File> files = this.getFilesByPostId(post.getId());
        if (files.isEmpty()) return;
        for (File file : files) {
            try {
                fileHandler.deleteFile(file.getFileUrl());
                fileRepository.delete(file);
            } catch (IOException e) {
                log.error("File not found : " + file.getFileUrl());
            }
        }
    }

    public void deleteFileFromUser(User user) {
        Optional<File> file = this.getFileByUserId(user.getId());
        if (file.isEmpty()) return;

        File targetFile = file.get();
        try {
            fileHandler.deleteFile(targetFile.getFileUrl());
            fileRepository.delete(targetFile);
        } catch (IOException e) {
            log.error("File not found : " + targetFile.getFileUrl());
        }
    }

    public void deleteTempFiles() {
        deleteTempFilesByHour(24);
    }

    public void deleteTempFilesByHour(int afterHour) {
        List<TempFile> tempFiles = tempFileService.getTempFiles();
        if (tempFiles.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        for (TempFile tempFile : tempFiles) {
            if (now.minusHours(afterHour).isAfter(tempFile.getCreatedAt())) {
                try {
                    fileHandler.deleteFile(tempFile.getFileUrl());
                    tempFileService.deleteTempFile(tempFile.getId());
                } catch (IOException e) {
                    log.error("Temp file not found : " + tempFile.getFileUrl());
                }
            }
        }
    }

    public void deleteUnusedFilesByPost(Post post, List<FileDto> prevFiles) {
        List<File> files = this.getFilesByPostId(post.getId());
        if (files.isEmpty()) return;
        // prevFiles에서 files와 겹치는 리스트 제거
        for (FileDto prevFile : prevFiles) {
            files.removeIf(file -> prevFile.getFileName().equals(file.getFileName()));
        }
        if (files.isEmpty()) return;
        for (File file : files) {
            try {
                log.info("Delete unused file : " + file.getFileUrl());
                fileHandler.deleteFile(file.getFileUrl());
                fileRepository.delete(file);
            } catch (IOException e) {
                log.error("File not found : " + file.getFileUrl());
            }
        }
    }

    public void deleteUnusedFilesByComment(Comment comment, List<FileDto> prevFiles) {
        List<File> files = this.getFilesByCommentId(comment.getId());
        if (files.isEmpty()) return;
        // prevFiles에서 files와 겹치는 리스트 제거
        for (FileDto prevFile : prevFiles) {
            files.removeIf(file -> prevFile.getFileName().equals(file.getFileName()));
        }
        if (files.isEmpty()) return;
        for (File file : files) {
            try {
                log.info("Delete unused file : " + file.getFileUrl());
                fileHandler.deleteFile(file.getFileUrl());
                fileRepository.delete(file);
            } catch (IOException e) {
                log.error("File not found : " + file.getFileUrl());
            }
        }
    }
}
