package io.blog.devlog.domain.like.repository;

import io.blog.devlog.domain.like.model.PostLike;
import io.blog.devlog.domain.like.model.PostLikeId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT pl FROM PostLike pl WHERE pl.id.postId = :postId")
    public List<PostLike> findByPost(@Param("postId") Long postId);
}
