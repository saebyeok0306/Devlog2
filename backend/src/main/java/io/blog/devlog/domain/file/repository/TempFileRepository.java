package io.blog.devlog.domain.file.repository;

import io.blog.devlog.domain.file.model.TempFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TempFileRepository extends JpaRepository<TempFile, Long> {
    public Optional<TempFile> findByFileUrl(String fileUrl);
}
