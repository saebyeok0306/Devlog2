package io.blog.devlog.domain.sitemap.controller;

import io.blog.devlog.domain.sitemap.dto.PostUrlDto;
import io.blog.devlog.domain.sitemap.service.SitemapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sitemap")
@Slf4j
public class SitemapController {

    private final SitemapService sitemapService;

    @PostMapping()
    void generateSitemap() {
        try {
            sitemapService.createAllPostToSubSitemap();
            log.info("Sitemap generated");
        } catch (Exception e) {
            log.error("Sitemap generation failed", e);
            throw e;
        }
    }

    @PostMapping("/post")
    void addPostSitemap(@RequestBody PostUrlDto postUrl) {
        try {
            sitemapService.addPostToSubSitemap(postUrl);
            log.info("Post sitemap added : " + postUrl.getUrl());
        } catch (Exception e) {
            log.error("Post sitemap add failed : " + postUrl.getUrl(), e);
            throw e;
        }
    }

    @DeleteMapping("/post")
    void deletePostSitemap(@RequestBody PostUrlDto postUrl) {
        try {
            sitemapService.deletePostFromSubSitemap(postUrl);
            log.info("Post sitemap deleted : " + postUrl.getUrl());
        } catch (Exception e) {
            log.error("Post sitemap delete failed : " + postUrl.getUrl(), e);
            throw e;
        }
    }
}
