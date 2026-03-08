package io.blog.devlog.domain.like.service;

import io.blog.devlog.domain.like.model.PostLikeDetail;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;

import java.util.List;

public interface PostLikeService {
    public int likeCountFromPost(Post post);
    public boolean isLikedPost(Post post, User user);
    public List<User> getLikersFromPostUrl(String postUrl);
    public PostLikeDetail likeDetailFromPost(Post post, User user);
    public void likePost(Post post, User user);
    public void unlikePost(Post post, User user);
}
