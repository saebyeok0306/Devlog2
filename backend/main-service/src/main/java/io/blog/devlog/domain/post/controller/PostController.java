package io.blog.devlog.domain.post.controller;

import io.blog.devlog.domain.comment.dto.ResponseCommentDto;
import io.blog.devlog.domain.comment.service.CommentService;
import io.blog.devlog.domain.like.model.PostLikeDetail;
import io.blog.devlog.domain.like.service.PostLikeService;
import io.blog.devlog.domain.post.dto.*;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.model.PostDetail;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.post.service.PostUploadService;
import io.blog.devlog.domain.post.service.PostViewsService;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.domain.views.service.PostViewCountService;
import io.blog.devlog.global.exception.NoPermissionException;
import io.blog.devlog.global.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final PostUploadService postUploadService;
    private final PostViewsService postViewsService;
    private final PostViewCountService postViewCountService;


    @Transactional
    @PostMapping
    public void uploadPost(@RequestBody RequestPostDto requestPostDto) {
        log.info("Post uploaded : " + requestPostDto.getTitle());
        postUploadService.savePost(requestPostDto);
    }

    @Transactional
    @PostMapping("/edit")
    public void editPost(@RequestBody RequestEditPostDto requestEditPostDto) {
        log.info("Post edited : " + requestEditPostDto.getTitle());
        Post post = postService.getPostById(requestEditPostDto.getId());
        if (post == null) {
            log.info("Post not found : " + requestEditPostDto.getId());
            throw new NotFoundException("Post not found : " + requestEditPostDto.getId());
        }
        postUploadService.editPost(post, requestEditPostDto);
    }

    @GetMapping
    public ResponseEntity<ResponsePageablePostDto> getPosts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Post> posts = postService.getPosts(pageable);
        List<ResponsePostNonContentDto> responsePostDtos = new ArrayList<>();
        for (Post post : posts) {
            responsePostDtos.add(ResponsePostNonContentDto.of(post));
        }
        ResponsePageablePostDto responsePageablePostDto = ResponsePageablePostDto.builder()
                .posts(responsePostDtos)
                .currentPage(posts.getNumber())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
        return ResponseEntity.ok(responsePageablePostDto);
    }

    @GetMapping("/inf")
    public ResponseEntity<ResponseSlicePostDto> getInfinitePosts(@RequestParam(defaultValue = "0") Long lastId,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("id").descending());
        Slice<Post> posts = postService.getInfinitePosts(pageable, lastId);
        List<Post> getPosts = posts.getContent();

        if (getPosts.isEmpty()) {
            return ResponseEntity.ok(ResponseSlicePostDto.builder().posts(Collections.emptyList()).lastId(0L).hasNext(false).build());
        }

        List<ResponsePostNonContentDto> responsePostDtos = new ArrayList<>();
        for (Post post : getPosts) {
            responsePostDtos.add(ResponsePostNonContentDto.of(post));
        }
        ResponseSlicePostDto responseSlicePostDto = ResponseSlicePostDto.builder()
                .posts(responsePostDtos)
                .lastId(getPosts.get(getPosts.size()-1).getId())
                .hasNext(posts.hasNext())
                .build();
        return ResponseEntity.ok(responseSlicePostDto);
    }

    @GetMapping("/{url}")
    public ResponseEntity<ResponsePostCommentDto> getPost(HttpServletRequest request, @PathVariable String url) {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email).orElse(User.builder()
                .email(null)
                .role(Role.GUEST)
                .username("GUEST")
                .build());
        PostDetail postDetail = postService.getPostByUrl(url, user);
        boolean increased = postViewsService.increaseViewCount(postDetail.getPost().getId(), request);
        if (increased) {
            postViewCountService.increaseViewCount(postDetail.getPost().getId());
        }
        List<ResponseCommentDto> comments = commentService.getCommentsFromPost(user, postDetail);
        PostLikeDetail postLikeDetail = postLikeService.likeDetailFromPost(postDetail.getPost(), user);
        return ResponseEntity.ok(ResponsePostCommentDto.of(email, postDetail, comments, postLikeDetail));
    }

    @GetMapping("/category/v1/{categoryName}")
    public ResponseEntity<ResponsePageablePostDto> getPostsByCategory(@PathVariable String categoryName,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Post> posts = postService.getPostsByCategory(categoryName, pageable);
        List<ResponsePostNonContentDto> responsePostDtos = new ArrayList<>();
        for (Post post : posts) {
            responsePostDtos.add(ResponsePostNonContentDto.of(post));
        }
        ResponsePageablePostDto responsePageablePostDto = ResponsePageablePostDto.builder()
                .posts(responsePostDtos)
                .currentPage(posts.getNumber())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
        return ResponseEntity.ok(responsePageablePostDto);
    }

    @GetMapping("/category/v2/{categoryId}")
    public ResponseEntity<ResponsePageablePostDto> getPostsByCategoryId(@PathVariable Long categoryId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Post> posts = postService.getPostsByCategoryId(categoryId, pageable);
        List<ResponsePostNonContentDto> responsePostDtos = new ArrayList<>();
        for (Post post : posts) {
            responsePostDtos.add(ResponsePostNonContentDto.of(post));
        }
        ResponsePageablePostDto responsePageablePostDto = ResponsePageablePostDto.builder()
                .posts(responsePostDtos)
                .currentPage(posts.getNumber())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
        return ResponseEntity.ok(responsePageablePostDto);
    }

    @Transactional
    @DeleteMapping("/{url}")
    public void deletePost(@PathVariable String url) {
        String email = getUserEmail();
        PostDetail postDetail = postService.getPostByUrl(email, url);
        if(!postDetail.getPost().getUser().getEmail().equals(email)) {
            throw new NoPermissionException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        commentService.deleteCommentsByPostId(postDetail.getPost().getId());
        postService.deletePost(postDetail.getPost());
    }

    @GetMapping("/{url}/permissions")
    public boolean checkPostPermission(@PathVariable String url) {
        String email = getUserEmail();
        Post post = postService.getSimplePostByUrl(url);
        return post.getUser().getEmail().equals(email);
    }

    @GetMapping("/{url}/metadata")
    public ResponsePostMetadataDto getPostMetadata(@PathVariable String url) {
        log.info("GET /posts/" + url + "/metadata");
        Post post = postService.getSimplePostByUrl(url);
        if (post == null) {
            throw new NotFoundException("Post not found : " + url);
        }
        return ResponsePostMetadataDto.of(post);
    }

    @GetMapping("/{url}/exists")
    public boolean isPostExists(@PathVariable String url) {
        return postService.isPostExists(url);
    }
}
