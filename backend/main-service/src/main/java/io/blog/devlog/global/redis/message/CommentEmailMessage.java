package io.blog.devlog.global.redis.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEmailMessage {
    private String email;
    private String subject;
    private String postName;
    private String postUrl;
    private String commentContent;
    private String commentAuthor;
}
