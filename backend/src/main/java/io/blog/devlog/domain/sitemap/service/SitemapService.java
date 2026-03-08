package io.blog.devlog.domain.sitemap.service;

import io.blog.devlog.domain.sitemap.dto.PostUrlDto;

public interface SitemapService {
    String generateSitemap();
    void generateSitemapXml(String sitemap);
    void addPostToSubSitemap(PostUrlDto postUrlDto);
    void deletePostFromSubSitemap(PostUrlDto postUrlDto);
    void createAllPostToSubSitemap();
}
