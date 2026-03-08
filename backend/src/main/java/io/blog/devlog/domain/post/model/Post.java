package io.blog.devlog.domain.post.model;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.like.model.PostLike;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.time.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name="POST", indexes = {
        @Index(name = "idx_url", columnList = "url"),
        @Index(name = "idx_category", columnList = "category_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class Post extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String url;
    private String title;
    @Column(length = 50000)
    private String content;
    private String previewUrl;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
//    @OneToMany(mappedBy = "post")
//    private List<File> files;
    private long views;
//    private long likes;
    private boolean hidden;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = false)
    private List<Comment> comments;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = false)
    private List<PostLike> likes;
//    private long comment;
//    private String tag;
}
