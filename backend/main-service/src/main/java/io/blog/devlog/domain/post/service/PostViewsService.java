package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface PostViewsService {
    public boolean increaseViewCount(Long postId, HttpServletRequest request);
}
