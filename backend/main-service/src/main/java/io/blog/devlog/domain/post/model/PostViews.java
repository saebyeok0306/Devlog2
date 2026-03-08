package io.blog.devlog.domain.post.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.sql.Timestamp;

@Table(name="POSTVIEWS", indexes = {
        @Index(name = "idx_post_view", columnList = "post_id"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class PostViews implements Persistable<PostViewsId> {
    @EmbeddedId
    private PostViewsId id;

//    @MapsId("postId")
//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;
//    @MapsId("userIp")
//    @JoinColumn(name = "user_ip")
//    private String user;
    @Setter
    private Timestamp viewsAt;

    @Override
    public boolean isNew() {
        return getViewsAt() == null;
    }
}
