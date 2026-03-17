package io.blog.devlog.domain.comment.controller;

import io.blog.devlog.domain.comment.dto.RequestGuestCommentDto;
import io.blog.devlog.domain.comment.dto.RequestGuestEditCommentDto;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.comment.service.GuestCommentService;
import io.blog.devlog.domain.post.model.PostDetail;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.global.exception.NoPermissionException;
import io.blog.devlog.global.redis.message.CommentEmailMessage;
import io.blog.devlog.global.redis.service.CommentEmailPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments/guest")
@Slf4j
public class GuestCommentController {
    private final PostService postService;
    private final GuestCommentService guestCommentService;
    private final CommentEmailPubService commentEmailPubService;

    @PostMapping
    public void uploadGuestComment(@RequestBody RequestGuestCommentDto requestCommentDto) {
        PostDetail postDetail = postService.getPostByUrl(requestCommentDto.getPostUrl());
        if (!postDetail.isCommentFlag()) {
            throw new NoPermissionException("댓글을 작성할 권한이 없습니다.");
        }
        guestCommentService.saveComment(requestCommentDto, postDetail.getPost());

        String contentTextOnly = requestCommentDto.getContent().replaceAll("<[^>]*>", "");
        CommentEmailMessage emailMessage = new CommentEmailMessage(
                postDetail.getPost().getUser().getEmail(),
                String.format("[devLog] %s 게시글에 익명댓글이 달렸습니다.", postDetail.getPost().getTitle()),
                postDetail.getPost().getTitle(),
                postDetail.getPost().getUrl(),
                contentTextOnly,
                requestCommentDto.getUsername()
        );
        commentEmailPubService.sendEmail(emailMessage);
    }

    @PostMapping("/{commentId}")
    public void updateComment(@RequestBody RequestGuestEditCommentDto requestEditCommentDto, @PathVariable Long commentId) {
        Comment comment = guestCommentService.updateComment(requestEditCommentDto, commentId);
        log.info("Update comment : " + comment);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @RequestParam String password) {
        guestCommentService.deleteComment(commentId, password);
    }
}
