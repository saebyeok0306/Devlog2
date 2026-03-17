package io.blog.devlog.domain.comment.service;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.comment.dto.RequestCommentDto;
import io.blog.devlog.domain.comment.dto.RequestEditCommentDto;
import io.blog.devlog.domain.comment.dto.ResponseCommentDto;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.comment.repository.CommentRepository;
import io.blog.devlog.domain.file.service.FileService;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.model.PostDetail;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.global.exception.NoPermissionException;
import io.blog.devlog.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final FileService fileService;
    private final PostService postService;

    public void saveComment(User user, RequestCommentDto requestCommentDto, Post post) {
        Long parentId = requestCommentDto.getParent();
        if (parentId != null && parentId != 0L) {
            commentRepository.findById(parentId).orElseThrow(() -> new NotFoundException("comment not found : " + parentId));
        }
        Comment entity = requestCommentDto.toEntity(user, post);
        log.info("Comment entity : " + entity);
        Comment comment = commentRepository.save(entity);
        fileService.uploadFileAndDeleteTempFile(comment, requestCommentDto.getFiles());
        fileService.deleteTempFiles(); // 임시파일 제거
        ResponseCommentDto.of(comment, post, user);
    }

    public Comment updateComment(RequestEditCommentDto requestEditCommentDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found : " + commentId));
        String email = getUserEmail();
        if (!comment.getUser().getEmail().equals(email)) {
            throw new NoPermissionException("댓글을 수정할 권한이 없습니다.");
        }
        comment = commentRepository.save(comment.toEdit(requestEditCommentDto));
        fileService.uploadFileAndDeleteTempFile(comment, requestEditCommentDto.getFiles());
        fileService.deleteTempFiles(); // 임시파일 제거
        fileService.deleteUnusedFilesByComment(comment, requestEditCommentDto.getFiles());
        return comment;
    }

    public List<ResponseCommentDto> getCommentsFromPost(User user, PostDetail postDetail) {
        if (postDetail.getPost().getCategory().getReadCategoryAuth().getKey() <= user.getRole().getKey()) {
            return this.filterCommentsByPermissions(user, postDetail.getPost().getId());
        }
        return Collections.emptyList();
    }

    public List<ResponseCommentDto> getCommentsFromPost(User user, Long postId) {
        Category category = postService.getCategoryByPostId(postId);
        if (category.getReadCategoryAuth().getKey() <= user.getRole().getKey()) {
            return this.filterCommentsByPermissions(user, postId);
        }
        return Collections.emptyList();
    }

    public List<ResponseCommentDto> filterCommentsByPermissions(User user, Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        if (comments.isEmpty()) return Collections.emptyList();
        Post post = comments.get(0).getPost();
        return comments.stream()
                .map(comment -> ResponseCommentDto.of(comment, post, user))
                .toList();
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found : " + commentId));
        this.deleteComment(comment);
    }

    public void deleteComment(Comment comment) {
        String email = getUserEmail();
        if (!comment.getUser().getEmail().equals(email)) {
            throw new NoPermissionException("댓글을 삭제할 권한이 없습니다.");
        }
        fileService.deleteFileFromComment(comment);
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

    public void deleteCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        comments.forEach(comment -> {
            fileService.deleteFileFromComment(comment);
            commentRepository.delete(comment);
        });
    }
}