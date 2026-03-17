package io.blog.devlog.domain.like.service;

import io.blog.devlog.domain.like.model.PostLike;
import io.blog.devlog.domain.like.model.PostLikeDetail;
import io.blog.devlog.domain.like.model.PostLikeId;
import io.blog.devlog.domain.like.repository.PostLikeRepository;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.user.dto.ResponseUserDto;
import io.blog.devlog.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

    @Override
    public int likeCountFromPost(Post post) {
        List<PostLike> postLikes = postLikeRepository.findByPost(post.getId());
        return postLikes.size();
    }

    @Override
    public boolean isLikedPost(Post post, User user) {
        Optional<PostLike> postLike = postLikeRepository.findById(new PostLikeId(post.getId(), user.getId()));
        return postLike.isPresent();
    }

    @Override
    public List<User> getLikersFromPostUrl(String postUrl) {
        Post post = postService.getSimplePostByUrl(postUrl);
        if (post != null) {
            List<PostLike> postLikes = postLikeRepository.findByPost(post.getId());
            return postLikes.stream().map(PostLike::getUser).toList();
        }
        return emptyList();
    }

    @Override
    public PostLikeDetail likeDetailFromPost(Post post, User user) {
        List<PostLike> postLikes = postLikeRepository.findByPost(post.getId());
        boolean isLiked = postLikes.stream().anyMatch(postLike -> postLike.getUser().getId().equals(user.getId()));
        return PostLikeDetail.builder()
                .likeCount(postLikes.size())
                .isLiked(isLiked)
                .users(postLikes.stream().map((postLike -> ResponseUserDto.of(postLike.getUser()))).toList())
                .build();
    }

    @Override
    public void likePost(Post post, User user) {
        postLikeRepository.save(PostLike.builder()
                .id(new PostLikeId(post.getId(), user.getId()))
                .post(post)
                .user(user)
                .build());
    }

    @Override
    public void unlikePost(Post post, User user) {
        postLikeRepository.deleteById(new PostLikeId(post.getId(), user.getId()));
    }
}
