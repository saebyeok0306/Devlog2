package io.blog.devlog.domain.comment.service;

import io.blog.devlog.domain.comment.dto.RequestGuestCommentDto;
import io.blog.devlog.domain.comment.dto.RequestGuestEditCommentDto;
import io.blog.devlog.domain.comment.dto.ResponseCommentDto;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.comment.repository.CommentRepository;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.exception.NoPermissionException;
import io.blog.devlog.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GuestCommentService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CommentRepository commentRepository;

    public void saveComment(RequestGuestCommentDto requestCommentDto, Post post) {
        Long parentId = requestCommentDto.getParent();
        if (parentId != null && parentId != 0L) {
            commentRepository.findById(parentId).orElseThrow(() -> new NotFoundException("comment not found : " + parentId));
        }
        commentRepository.save(requestCommentDto.toEntity(bCryptPasswordEncoder, post));
    }

    public Comment updateComment(RequestGuestEditCommentDto requestEditCommentDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found : " + commentId));
        if (comment.getUser() != null) {
            throw new NoPermissionException("익명 댓글이 아닙니다.");
        }
        if (!bCryptPasswordEncoder.matches(requestEditCommentDto.getPassword(), comment.getGuest().getPassword())) {
            throw new NoPermissionException("비밀번호가 일치하지 않습니다.");
        }
        comment = commentRepository.save(comment.toGuestEdit(requestEditCommentDto));
        return comment;
    }

    public void deleteComment(Long commentId, String password) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found : " + commentId));
        if (comment.getUser() != null) {
            throw new NoPermissionException("익명 댓글이 아닙니다.");
        }
        if (!bCryptPasswordEncoder.matches(password, comment.getGuest().getPassword())) {
            throw new NoPermissionException("비밀번호가 일치하지 않습니다.");
        }
        boolean isParent = commentRepository.existsByParent(comment.getId());
        if (isParent) {
            comment.setDeleted(true);
            comment.setContent("삭제된 댓글입니다.");
            comment.setUser(null);
            commentRepository.save(comment);
        } else {
            commentRepository.delete(comment);
        }
    }
}
