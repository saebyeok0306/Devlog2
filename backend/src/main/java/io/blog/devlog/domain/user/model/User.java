package io.blog.devlog.domain.user.model;

import io.blog.devlog.domain.like.model.PostLike;
import io.blog.devlog.domain.user.dto.RequestPasswordDto;
import io.blog.devlog.global.exception.NotMatchPasswordException;
import io.blog.devlog.global.time.BaseTime;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

// 기본 생성자의 접근 수준을 Protected로 설정합니다.
@Table(name="USERS", indexes = {
        @Index(name = "idx_email", columnList = "email"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@ToString
@Builder
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Column(unique = true)
    private String email;
    @Setter
    private String about;
    @Nullable
    @Column(name = "profile_url")
    private String profileUrl;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.GUEST;
    @Nullable
    private String provider;
    @Nullable
    @Column(name = "provider_id")
    private String providerId;
    @NotNull
    @ColumnDefault("0") // false
    @Builder.Default
    private Boolean certificate = false;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = false)
    private List<PostLike> likes;

    public void passwordEncode(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.password = bCryptPasswordEncoder.encode(this.password);
    }

    public void updatePassword(BCryptPasswordEncoder bCryptPasswordEncoder, RequestPasswordDto requestPasswordDto) {
        if (!bCryptPasswordEncoder.matches(requestPasswordDto.getCurrentPassword(), this.password)) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
        this.password = bCryptPasswordEncoder.encode(requestPasswordDto.getNewPassword());
    }

    public void updateProfile(String url) {
        this.profileUrl = url;
    }

    public void certified() {
        this.certificate = true;
        this.role = Role.USER;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
