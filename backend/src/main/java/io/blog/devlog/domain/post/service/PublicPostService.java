package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.post.model.Post;

public interface PublicPostService {
    public boolean isPubliclyVisible(Post post);
}
