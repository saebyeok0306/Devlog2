package io.blog.devlog.domain.comment.model;

import io.blog.devlog.domain.comment.dto.RequestEditCommentDto;
import io.blog.devlog.domain.comment.dto.RequestGuestEditCommentDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.time.BaseTime;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Table(name="COMMENTS", indexes = {
        @Index(name = "idx_post_id", columnList = "post_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "guest_id")
    private GuestDetail guest;
    @Nullable
    @JoinColumn(name = "parent_id")
    private Long parent; // 대댓글
    @Setter
    @Column(length = 5000)
    private String content;
    private boolean hidden;
    @Setter
    @ColumnDefault("0") // false
    @Builder.Default
    private boolean deleted = false;

    public Comment toEdit(RequestEditCommentDto requestEditCommentDto) {
        this.content = requestEditCommentDto.getContent();
        this.hidden = requestEditCommentDto.isHidden();
        return this;
    }

    public Comment toGuestEdit(RequestGuestEditCommentDto requestGuestEditCommentDto) {
        this.content = requestGuestEditCommentDto.getContent();
        this.hidden = requestGuestEditCommentDto.isHidden();
        return this;
    }
}
