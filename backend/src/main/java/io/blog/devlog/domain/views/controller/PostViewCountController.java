package io.blog.devlog.domain.views.controller;

import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.views.dto.PostViewCountDto;
import io.blog.devlog.domain.views.dto.ResponsePostViewCountDto;
import io.blog.devlog.domain.views.service.PostViewCountService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/views/post")
@RequiredArgsConstructor
public class PostViewCountController {
    private final PostService postService;
    private final PostViewCountService postViewCountService;

    @GetMapping("/{postUrl}/daily")
    public ResponsePostViewCountDto getDailyPostViewCount(@PathVariable String postUrl, @RequestParam String start, @RequestParam String end) throws BadRequestException {
        Post post = postService.getSimplePostByUrl(postUrl);
        List<PostViewCountDto> postViewCountList = postViewCountService.getDailyPostViewCount(post.getId(), start, end);
        return ResponsePostViewCountDto.of(post, postViewCountList);
    }

    @GetMapping("/{postUrl}/monthly")
    public ResponsePostViewCountDto getMonthlyPostViewCount(@PathVariable String postUrl, @RequestParam String start, @RequestParam String end) {
        Post post = postService.getSimplePostByUrl(postUrl);
        List<PostViewCountDto> postViewCountList = postViewCountService.getMonthlyPostViewCount(post.getId(), start, end);
        return ResponsePostViewCountDto.of(post, postViewCountList);
    }

    @GetMapping("/{postUrl}/yearly")
    public ResponsePostViewCountDto getYearlyPostViewCount(@PathVariable String postUrl, @RequestParam String start, @RequestParam String end) {
        Post post = postService.getSimplePostByUrl(postUrl);
        List<PostViewCountDto> postViewCountList = postViewCountService.getYearlyPostViewCount(post.getId(), start, end);
        return ResponsePostViewCountDto.of(post, postViewCountList);
    }
}
