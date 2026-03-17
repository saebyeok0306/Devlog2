package io.blog.devlog.domain.post.repository;

import io.blog.devlog.domain.post.model.PostViews;
import io.blog.devlog.domain.post.model.PostViewsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewsRepository extends JpaRepository<PostViews, PostViewsId> {

}
