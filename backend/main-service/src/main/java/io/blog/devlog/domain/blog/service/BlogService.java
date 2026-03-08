package io.blog.devlog.domain.blog.service;

import io.blog.devlog.domain.blog.dto.RequestInfoDto;
import io.blog.devlog.domain.blog.dto.ResponseInfoDto;
import io.blog.devlog.domain.blog.model.Blog;

public interface BlogService {
    public Blog getBlog();
    public ResponseInfoDto getBlogInfo();
    public void updateBlogInfo(RequestInfoDto requestInfoDto);
    public void increaseVisitCount();
}
