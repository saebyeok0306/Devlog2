package io.blog.devlog.domain.comment.controller;

import io.blog.devlog.domain.comment.dto.RequestCommentDto;
import io.blog.devlog.domain.comment.dto.RequestEditCommentDto;
import io.blog.devlog.domain.comment.dto.ResponseCommentDto;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.comment.service.CommentService;
import io.blog.devlog.domain.post.model.PostDetail;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.exception.NoPermissionException;
import io.blog.devlog.global.exception.NotFoundException;
import io.blog.devlog.global.redis.message.CommentEmailMessage;
import io.blog.devlog.global.redis.service.CommentEmailPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final CommentEmailPubService commentEmailPubService;

    @PostMapping
    public void uploadComment(@RequestBody RequestCommentDto requestCommentDto) {
        log.info("RequestCommentDto : " + requestCommentDto);
        String email = getUserEmail();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found : " + email));

        PostDetail postDetail = postService.getPostByUrl(requestCommentDto.getPostUrl(), user);
        if (!postDetail.isCommentFlag()) {
            throw new NoPermissionException("댓글을 작성할 권한이 없습니다.");
        }
        commentService.saveComment(user, requestCommentDto, postDetail.getPost());

        String contentTextOnly = requestCommentDto.getContent().replaceAll("<[^>]*>", "");
        CommentEmailMessage emailMessage = new CommentEmailMessage(
                postDetail.getPost().getUser().getEmail(),
                String.format("[devLog] %s 게시글에 댓글이 달렸습니다.", postDetail.getPost().getTitle()),
                postDetail.getPost().getTitle(),
                postDetail.getPost().getUrl(),
                contentTextOnly,
                user.getUsername()
        );
        commentEmailPubService.sendEmail(emailMessage);
    }

    @PostMapping("/{commentId}")
    public void updateComment(@RequestBody RequestEditCommentDto requestEditCommentDto, @PathVariable Long commentId) {
        Comment comment = commentService.updateComment(requestEditCommentDto, commentId);
        log.info("Update comment : " + comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ResponseCommentDto>> getCommentsByPost(@PathVariable Long postId) {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email).orElse(User.builder()
                .email(null)
                .role(Role.GUEST)
                .username("GUEST")
                .build());
        return ResponseEntity.ok(commentService.getCommentsFromPost(user, postId));
    }
}
