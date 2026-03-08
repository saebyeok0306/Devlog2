package io.blog.devlog.domain.category.repository;

import io.blog.devlog.domain.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Modifying
    @Query(value="TRUNCATE TABLE Category", nativeQuery=true)
    public void truncate();

    public Optional<Category> findByName(String name);
}
