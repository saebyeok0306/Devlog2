package io.blog.devlog.domain.comment.repository;

import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.comment.model.GuestDetail;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static io.blog.devlog.utils.EntityFactory.createPost;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void test() {
        // given
        GuestDetail guest = GuestDetail.builder()
                .username("username")
                .password("password")
                .build();
        Comment comment = Comment.builder()
                .content("test")
                .guest(guest)
                .deleted(false)
                .hidden(false)
                .parent(0L)
                .build();

        // when
        comment = commentRepository.save(comment);
        entityManager.flush(); // DB에 반영
        entityManager.clear(); // 영속성 컨텍스트 초기화

        // then
        Optional<Comment> optionalComment = commentRepository.findById(comment.getId());
        if (optionalComment.isPresent()) {
            Comment getComment = optionalComment.get();
            System.out.println(getComment);
            System.out.println(getComment.getUser());
        }
    }
}
