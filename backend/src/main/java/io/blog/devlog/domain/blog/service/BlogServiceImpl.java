package io.blog.devlog.domain.blog.service;

import io.blog.devlog.domain.blog.dto.RequestInfoDto;
import io.blog.devlog.domain.blog.dto.ResponseInfoDto;
import io.blog.devlog.domain.blog.model.Blog;
import io.blog.devlog.domain.blog.repository.BlogRepository;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserService userService;

    @Override
    public Blog getBlog() {
        return blogRepository.findById(1L).orElseGet(() -> {
            User user = userService.getAdmin();
            Blog newBlog = Blog.builder()
                    .id(1L)
                    .visit(0L)
                    .about("")
                    .profileUrl(null)
                    .user(user)
                    .build();
            return blogRepository.save(newBlog);
        });
    }

    @Override
    public ResponseInfoDto getBlogInfo() {
        return ResponseInfoDto.toDto(this.getBlog());
    }

    @Override
    public void updateBlogInfo(RequestInfoDto requestInfoDto) {
        Blog blog = this.getBlog();
        blog.setAbout(requestInfoDto.getAbout());
        blog.setProfileUrl(requestInfoDto.getProfileUrl());
        blogRepository.save(blog);
    }

    @Override
    public void increaseVisitCount() {
        Blog blog = this.getBlog();
        blog.setVisit(blog.getVisit()+1);
        blogRepository.save(blog);
    }
}
