package io.blog.devlog.domain.sitemap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SitemapMessage implements Serializable {
    private String action; // "ADD", "DELETE"
    private Long categoryId;
    private String url;
}