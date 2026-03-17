package io.blog.devlog.domain.sitemap.service;

import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.repository.PostRepository;
import io.blog.devlog.domain.sitemap.dto.PostUrlDto;
import io.blog.devlog.domain.sitemap.dto.SitemapMessage;
import io.blog.devlog.domain.user.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SitemapServiceImpl implements SitemapService {

    private final PostRepository postRepository;

    @Value("${sitemap.path}")
    private String sitemapPath;

    @Value("${sitemap.resource.path}")
    private String sitemapResourcePath;

    private static final String siteUrl = "https://devlog.run";

    // Redis 메시지를 수신하는 메서드
    public void handleMessage(SitemapMessage message) {
        PostUrlDto dto = PostUrlDto.of(message.getCategoryId(), message.getUrl());
        if ("DELETE".equals(message.getAction())) {
            this.deletePostFromSubSitemap(dto);
        } else {
            this.addPostToSubSitemap(dto);
        }
    }

    @Override
    public String generateSitemap() {
        var sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n\n");
        sb.append("<url><loc>").append(siteUrl).append("</loc></url>\n");

        Path location = this.getFolder(sitemapResourcePath);
        File folder = location.toFile();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File subSitemap : files) {
                try {
                    List<String> lines = Files.readAllLines(subSitemap.toPath());
                    for (String line : lines) {
                        sb.append("<url><loc>").append(line).append("</loc></url>\n");
                    }
                } catch (IOException e) {
                    log.error("Failed to read file: {}", subSitemap, e);
                }
            }
        }

        sb.append("</urlset>");
        return sb.toString();
    }

    @Override
    public void generateSitemapXml(String sitemap) {
        Path location = this.getFolder(sitemapPath);
        Path sitemapFile = this.getSitemap(location, "sitemap.xml");

        try {
            Files.write(sitemapFile, sitemap.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to write sitemap.xml", e);
        }
    }

    @Override
    public void addPostToSubSitemap(PostUrlDto postUrlDto) {
        Path location = this.getFolder(sitemapResourcePath);
        Path subSitemapFile = this.getSitemap(location, "sub_sitemap_" + postUrlDto.getCategoryId() + ".xml");

        String url = this.getSitemapUrl(postUrlDto.getUrl());
        boolean flag = true;
        try {
            List<String> lines = Files.readAllLines(subSitemapFile);
            for (String line : lines) {
                if (line.equals(url)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Files.write(subSitemapFile, (url + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            log.error("Failed to update sub-sitemap: {}", subSitemapFile, e);
        }

        String generatedSitemap = this.generateSitemap();
        this.generateSitemapXml(generatedSitemap);
    }

    @Override
    public void deletePostFromSubSitemap(PostUrlDto postUrlDto) {
        Path location = this.getFolder(sitemapResourcePath);
        Path subSitemapFile = this.getSitemap(location, "sub_sitemap_" + postUrlDto.getCategoryId() + ".xml");

        String url = this.getSitemapUrl(postUrlDto.getUrl());

        try {
            List<String> lines = Files.readAllLines(subSitemapFile);
            List<String> newLines = lines.stream()
                    .filter(line -> !line.equals(url))
                    .collect(Collectors.toList());

            if (newLines.isEmpty()) {
                Files.delete(subSitemapFile);
            } else {
                Files.write(subSitemapFile, newLines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Failed to delete from sub-sitemap: {}", subSitemapFile, e);
        }

        String generatedSitemap = this.generateSitemap();
        this.generateSitemapXml(generatedSitemap);
    }

    @Override
    public void createAllPostToSubSitemap() {
        int page = 0;
        int size = 5;
        Page<Post> postsPage = postRepository.findAllPagePublicPosts(PageRequest.of(page, size), Role.GUEST);

        processPosts(postsPage.getContent());

        if (postsPage.getTotalPages() > 1) {
            for (int i = 1; i < postsPage.getTotalPages(); i++) {
                Page<Post> currentPage = postRepository.findAllPagePublicPosts(PageRequest.of(i, size), Role.GUEST);
                processPosts(currentPage.getContent());
            }
        }
    }

    private void processPosts(List<Post> posts) {
        for (Post post : posts) {
            PostUrlDto postUrlDto = PostUrlDto.builder()
                    .categoryId(post.getCategory().getId())
                    .url(post.getUrl())
                    .build();
            this.addPostToSubSitemap(postUrlDto);
        }
    }

    Path getFolder(String location) {
        Path path = Paths.get(location);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.info("Failed to create directory: " + path);
            }
        }
        return path;
    }

    Path getSitemap(Path directoryPath, String filename) {
        var subSitemapFile = directoryPath.resolve(filename);

        if (!Files.exists(subSitemapFile)) {
            try {
                Files.createFile(subSitemapFile);
            } catch (IOException e) {
                log.info("Failed to create file: " + subSitemapFile);
            }
        }
        return subSitemapFile;
    }

    String getSitemapUrl(String url) {
        // return "${siteUrl}/post/$url"
        return siteUrl + "/post/" + url;
    }
}
