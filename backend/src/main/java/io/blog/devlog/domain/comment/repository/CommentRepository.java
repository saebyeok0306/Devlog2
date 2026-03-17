package io.blog.devlog.domain.comment.repository;

import io.blog.devlog.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /* 해당 글의 카테고리 읽기 권한 체크 */
    /* 비밀댓글 : 글쓴사람인지, 댓글쓴사람인지, 관리자인지 */
    @EntityGraph(attributePaths = {"user", "post"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    public List<Comment> findAllByPostId(@Param("postId") Long postId);
    public boolean existsByParent(Long parent);
}
