package io.blog.devlog.utils;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class EntityFactoryTest {

    @Test
    void createUser() {
        // given
        User user = EntityFactory.createUser();

        // then
        assertThat(user).isInstanceOf(User.class);
        assertThat(user.getEmail().length()).isGreaterThan(0);
        assertThat(user.getUsername().length()).isGreaterThan(0);
        assertThat(user.getRole()).isEqualTo(Role.GUEST);
    }

    @Test
    void createUser2() {
        // given
        User user = EntityFactory.createUser("test", "test@gmail.com" ,Role.ADMIN);

        // then
        assertThat(user).isInstanceOf(User.class);
        assertThat(user.getEmail()).isEqualTo("test@gmail.com");
        assertThat(user.getUsername()).isEqualTo("test");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void createCategory() {
        // given
        Category category = EntityFactory.createCategory();

        // then
        assertThat(category).isInstanceOf(Category.class);
        assertThat(category.getName().length()).isGreaterThan(0);
    }

    @Test
    void createCategory2() {
        // given
        Category category = EntityFactory.createCategory(1L, "test", Role.GUEST, Role.USER, Role.ADMIN);

        // then
        assertThat(category).isInstanceOf(Category.class);
        assertThat(category.getLayer()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("test");
        assertThat(category.getWritePostAuth()).isEqualTo(Role.GUEST);
        assertThat(category.getWriteCommentAuth()).isEqualTo(Role.USER);
        assertThat(category.getReadCategoryAuth()).isEqualTo(Role.ADMIN);
    }

    @Test
    void createPost() {
        // given
        Post post = EntityFactory.createPost();

        // then
        assertThat(post).isInstanceOf(Post.class);
        assertThat(post.getUser()).isInstanceOf(User.class);
        assertThat(post.getCategory()).isInstanceOf(Category.class);
        assertThat(post.getUrl().length()).isGreaterThan(0);
    }

    @Test
    void createPost2() {
        // given
        User user = EntityFactory.createUser();
        Category category = EntityFactory.createCategory();
        Post post = EntityFactory.createPost("test", "title", user, category);

        // then
        assertThat(post).isInstanceOf(Post.class);
        assertThat(post.getUser()).isEqualTo(user);
        assertThat(post.getCategory()).isEqualTo(category);
        assertThat(post.getUrl()).isEqualTo("test");
        assertThat(post.getTitle()).isEqualTo("title");
    }

    @Test
    void createComment() {
        // given
        Comment comment = EntityFactory.createComment();

        // then
        assertThat(comment).isInstanceOf(Comment.class);
        assertThat(comment.getUser()).isInstanceOf(User.class);
        assertThat(comment.getPost()).isInstanceOf(Post.class);
    }

    @Test
    void createComment2() {
        // given
        User user = EntityFactory.createUser();
        Post post = EntityFactory.createPost();
        Comment comment = EntityFactory.createComment("test", user, post);

        // then
        assertThat(comment).isInstanceOf(Comment.class);
        assertThat(comment.getUser()).isEqualTo(user);
        assertThat(comment.getPost()).isEqualTo(post);
        assertThat(comment.getContent()).isEqualTo("test");
    }
}
