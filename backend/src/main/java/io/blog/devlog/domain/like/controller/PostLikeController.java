package io.blog.devlog.domain.like.controller;

import io.blog.devlog.domain.like.dto.ResponsePostLikeCountDto;
import io.blog.devlog.domain.like.dto.ResponsePostLikerDto;
import io.blog.devlog.domain.like.service.PostLikeService;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.user.dto.ResponseUserDto;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@RequiredArgsConstructor
@RestController
@RequestMapping("/like/post")
@Slf4j
public class PostLikeController {
    private final UserService userService;
    private final PostService postService;
    private final PostLikeService postLikeService;

    @PutMapping("/{postUrl}")
    public void likePost(@PathVariable String postUrl) {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new UnauthorizedAccessException("로그인 후 이용해주세요."));
        Post post = postService.getSimplePostByUrl(postUrl);
        postLikeService.likePost(post, user);
    }

    @DeleteMapping("/{postUrl}")
    public void unlikePost(@PathVariable String postUrl) {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new UnauthorizedAccessException("로그인 후 이용해주세요."));
        Post post = postService.getSimplePostByUrl(postUrl);
        postLikeService.unlikePost(post, user);
    }

    @GetMapping("/{postUrl}/count")
    public ResponsePostLikeCountDto getLikeCountFromPostUrl(@PathVariable String postUrl) {
        Post post = postService.getSimplePostByUrl(postUrl);
        int likeCount = postLikeService.likeCountFromPost(post);
        return ResponsePostLikeCountDto.builder()
                .postUrl(postUrl)
                .likeCount(likeCount)
                .build();
    }

    @GetMapping("/{postUrl}/liker")
    public ResponsePostLikerDto getLikerFromPostUrl(@PathVariable String postUrl) {
        List<User> users = postLikeService.getLikersFromPostUrl(postUrl);
        return ResponsePostLikerDto.builder()
                .postUrl(postUrl)
                .users(users.stream().map(ResponseUserDto::of).toList())
                .build();
    }
}
