package io.blog.devlog.utils;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.comment.model.Comment;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import net.datafaker.Faker;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class EntityFactory {

    public static User createUser(String name, String email, Role role) {
        Faker faker = new Faker(new Locale("ko"));
        return User.builder()
                .id(new Random().nextLong())
                .username(name != null ? name : faker.name().fullName())
                .email(email != null ? email : UUID.randomUUID().toString().replaceAll("-", "") + "@gmail.com")
                .role(role != null ? role : Role.GUEST)
                .build();
    }

    public static User createUser() {
        Faker faker = new Faker(new Locale("ko"));
        return User.builder()
                .id(new Random().nextLong())
                .username(faker.name().fullName())
                .email(UUID.randomUUID().toString().replaceAll("-", "") + "@gmail.com")
                .role(Role.GUEST)
                .build();
    }

    public static Category createCategory(Long layer, String name, Role writePost, Role writeComment, Role readCategory) {
        Faker faker = new Faker(new Locale("ko"));
        return Category.builder()
                .layer(layer != null ? layer : new Random().nextInt(100000))
                .name(name != null ? name : faker.starCraft().character())
                .writePostAuth(writePost != null ? writePost : Role.GUEST)
                .writeCommentAuth(writeComment != null ? writeComment : Role.GUEST)
                .readCategoryAuth(readCategory != null ? readCategory : Role.GUEST)
                .build();
    }

    public static Category createCategory() {
        Faker faker = new Faker(new Locale("ko"));
        return Category.builder()
                .layer(new Random().nextInt(100000))
                .name(faker.starCraft().character())
                .writePostAuth(Role.GUEST)
                .writeCommentAuth(Role.GUEST)
                .readCategoryAuth(Role.GUEST)
                .build();
    }

    public static Post createPost(String url, String title, User user, Category category) {
        Faker faker = new Faker(new Locale("ko"));
        return Post.builder()
                .id(new Random().nextLong())
                .url(url != null ? url : faker.starCraft().unit())
                .title(title != null ? title : faker.beer().name())
                .content(faker.redDeadRedemption2().settlement())
                .user(user != null ? user : EntityFactory.createUser())
                .category(category != null ? category : EntityFactory.createCategory())
                .build();
    }

    public static Post createPost() {
        Faker faker = new Faker(new Locale("ko"));
        return Post.builder()
                .id(new Random().nextLong())
                .url(faker.starCraft().unit())
                .title(faker.beer().name())
                .content(faker.redDeadRedemption2().settlement())
                .user(EntityFactory.createUser())
                .category(EntityFactory.createCategory())
                .build();
    }

    public static Comment createComment(String content, User user, Post post) {
        Faker faker = new Faker(new Locale("ko"));
        return Comment.builder()
                .id(new Random().nextLong())
                .content(content != null ? content : faker.redDeadRedemption2().settlement())
                .user(user != null ? user : EntityFactory.createUser(null, null, null))
                .post(post != null ? post : EntityFactory.createPost(null, null, null, null))
                .deleted(false)
                .hidden(false)
                .parent(0L)
                .build();
    }

    public static Comment createComment() {
        Faker faker = new Faker(new Locale("ko"));
        return Comment.builder()
                .id(new Random().nextLong())
                .content(faker.redDeadRedemption2().settlement())
                .user(EntityFactory.createUser())
                .post(EntityFactory.createPost())
                .deleted(false)
                .hidden(false)
                .build();
    }
}
