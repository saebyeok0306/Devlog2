package io.blog.devlog.global.client;

import io.blog.devlog.domain.post.dto.ResponsePostUrlDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sitemap-service")
public interface SitemapClient {
    @PostMapping("/sitemap/post")
    public void addPostSitemap(@RequestBody ResponsePostUrlDto postUrl);
    @DeleteMapping("/sitemap/post")
    public void deletePostSitemap(@RequestBody ResponsePostUrlDto postUrl);
}
