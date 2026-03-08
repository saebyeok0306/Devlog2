package io.blog.devlog.domain.category.model;

import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.RoleConverter;
import io.blog.devlog.global.time.CreateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class Category extends CreateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @NotNull
    private long layer; // 카테고리 순서
    @Setter
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    @Convert(converter = RoleConverter.class)
    @Column(name = "write_post_auth")
    private Role writePostAuth = Role.GUEST;
    @NotNull
    @Convert(converter = RoleConverter.class)
    @Column(name = "write_comment_auth")
    private Role writeCommentAuth = Role.GUEST;
    @NotNull
    @Convert(converter = RoleConverter.class)
    @Column(name = "read_category_auth")
    private Role readCategoryAuth = Role.GUEST;
    @Setter
    private LocalDateTime lastPostAt;
}
