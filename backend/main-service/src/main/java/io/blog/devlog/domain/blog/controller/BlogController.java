package io.blog.devlog.domain.blog.controller;

import io.blog.devlog.domain.blog.dto.ResponseBlogVisitCountDto;
import io.blog.devlog.domain.blog.model.Blog;
import io.blog.devlog.domain.blog.service.BlogService;
import io.blog.devlog.domain.post.service.PostViewsService;
import io.blog.devlog.domain.views.dto.PostViewCountDto;
import io.blog.devlog.domain.views.service.PostViewCountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController {
    /* PostId 0번은 쓰지 않기때문에, 0번을 블로그 자체를 가리키게 하고 0번 id로 방문수를 체크함 */
    private final BlogService blogService;
    private final PostViewsService postViewsService;
    private final PostViewCountService postViewCountService;

    @PutMapping("/visit")
    public void visitBlog(HttpServletRequest request) {
        boolean isNotVisit = postViewsService.increaseViewCount(0L, request); // 중복방문 배제 기록
        if (isNotVisit) {
            blogService.increaseVisitCount(); // 총합 기록
            postViewCountService.increaseViewCount(0L); // 날짜별 기록
        }
    }

    @GetMapping("/visit/daily")
    public ResponseBlogVisitCountDto getDailyVisitCount(@RequestParam String start, @RequestParam String end) {
        List<PostViewCountDto> postViewCountList = postViewCountService.getDailyPostViewCount(0L, start, end);
        Blog blog = blogService.getBlog();
        return ResponseBlogVisitCountDto.builder()
                .totalCount(blog.getVisit())
                .visitCounts(postViewCountList)
                .build();
    }

    @GetMapping("/visit/monthly")
    public ResponseBlogVisitCountDto getMonthlyVisitCount(@RequestParam String start, @RequestParam String end) {
        List<PostViewCountDto> postViewCountList = postViewCountService.getMonthlyPostViewCount(0L, start, end);
        Blog blog = blogService.getBlog();
        return ResponseBlogVisitCountDto.builder()
                .totalCount(blog.getVisit())
                .visitCounts(postViewCountList)
                .build();
    }

    @GetMapping("/visit/yearly")
    public ResponseBlogVisitCountDto getYearlyVisitCount(@RequestParam String start, @RequestParam String end) {
        List<PostViewCountDto> postViewCountList = postViewCountService.getYearlyPostViewCount(0L, start, end);
        Blog blog = blogService.getBlog();
        return ResponseBlogVisitCountDto.builder()
                .totalCount(blog.getVisit())
                .visitCounts(postViewCountList)
                .build();
    }
}
