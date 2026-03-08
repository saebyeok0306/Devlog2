package io.blog.devlog.domain.file.repository;

import io.blog.devlog.domain.file.model.EntityType;
import io.blog.devlog.domain.file.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("SELECT f FROM File f WHERE f.entityType =:entityType AND f.entityId =:entityId")
    public List<File> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
    public Optional<File> findByFileUrl(String fileUrl);
}
