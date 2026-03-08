package io.blog.devlog.domain.post.dto;

import io.blog.devlog.domain.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostMetadataDto {
    private String title;
    private String description;
    private String previewUrl;
    private String author;
    private List<String> keywords;

    public static ResponsePostMetadataDto of(Post post) {
        String content = post.getContent().replaceAll("<[^>]*>", "");
        content = StringEscapeUtils.unescapeHtml(content);
        // content의 길이를 150자로 제한하고, 넘으면 ...으로 생략
        if (content.length() > 150) {
            content = content.substring(0, 150) + "...";
        }
        return ResponsePostMetadataDto.builder()
                .title(post.getTitle())
                .description(content)
                .previewUrl(post.getPreviewUrl())
                .author(post.getUser().getUsername())
                .keywords(Collections.emptyList())
                .build();
    }
}
