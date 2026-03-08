package io.blog.devlog.domain.category.dto;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private long layer; // 카테고리 순서
    private String name;
    private boolean isNewPostIcon;

    public static CategoryDto of(Category category) {
        LocalDateTime now = LocalDateTime.now();
        return CategoryDto.builder()
                .id(category.getId())
                .layer(category.getLayer())
                .name(category.getName())
                .isNewPostIcon(category.getLastPostAt() != null && category.getLastPostAt().plusDays(3).isAfter(now))
                .build();
    }

    public static CategoryDto of(Category category, LocalDateTime now) {
        return CategoryDto.builder()
                .id(category.getId())
                .layer(category.getLayer())
                .name(category.getName())
                .isNewPostIcon(category.getLastPostAt() != null && category.getLastPostAt().plusDays(3).isAfter(now))
                .build();
    }
}
