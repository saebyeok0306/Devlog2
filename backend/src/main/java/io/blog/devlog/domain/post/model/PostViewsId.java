package io.blog.devlog.domain.post.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class PostViewsId  implements Serializable {
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "user_ip")
    private String userIp;

    public PostViewsId() {}

    public PostViewsId(Long postId, String userIp) {
        this.postId = postId;
        this.userIp = userIp;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostViewsId that = (PostViewsId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(userIp, that.userIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userIp);
    }
}
