package io.blog.devlog.domain.blog.model;

import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.time.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class Blog extends BaseTime {
    @Id
    private Long id;
    @Setter
    private Long visit; // Blog Visit Count
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @Setter
    private String about; // 블로그 소개글
    @Setter
    @Column(name = "profile_url")
    private String profileUrl; // 블로그 프로필 사진
}
