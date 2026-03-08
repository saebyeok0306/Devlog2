package io.blog.devlog.domain.blog.controller;

import io.blog.devlog.domain.blog.dto.RequestInfoDto;
import io.blog.devlog.domain.blog.dto.ResponseInfoDto;
import io.blog.devlog.domain.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/info")
public class InfoController {
    private final BlogService blogService;

    @GetMapping
    public ResponseInfoDto getInfo() {
        return blogService.getBlogInfo();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void updateInfo(@RequestBody RequestInfoDto requestInfoDto) {
        blogService.updateBlogInfo(requestInfoDto);
    }
}
