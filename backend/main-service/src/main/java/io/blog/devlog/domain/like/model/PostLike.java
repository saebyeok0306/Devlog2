package io.blog.devlog.domain.like.model;

import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.time.CreateTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Table(name="POSTLIKE", indexes = {
        @Index(name = "idx_post", columnList = "post_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class PostLike extends CreateTime implements Persistable<PostLikeId> {
    @EmbeddedId
    private PostLikeId id;

    @ToString.Exclude
    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ToString.Exclude
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override // 새로운 엔티티 판단 전략 재정의
    public boolean isNew() {
        // createdAt 값이 null이면 새로운 Entity로 판단할 수 있다.
        return getCreatedAt() == null;
    }
}

// Reference: https://stackoverflow.com/questions/63999885/how-to-use-namedentitygraph-with-embeddedid
// https://ykh6242.tistory.com/entry/JPA-%EC%97%94%ED%8B%B0%ED%8B%B0-%EB%B3%B5%ED%95%A9%ED%82%A4Composite-Primary-Keys-%EB%A7%A4%ED%95%91