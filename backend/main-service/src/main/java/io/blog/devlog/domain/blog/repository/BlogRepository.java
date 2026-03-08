package io.blog.devlog.domain.blog.repository;

import io.blog.devlog.domain.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
