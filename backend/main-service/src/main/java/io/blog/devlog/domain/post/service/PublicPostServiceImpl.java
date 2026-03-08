package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.category.service.CategoryService;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublicPostServiceImpl implements PublicPostService {
    private final CategoryService categoryService;

    @Override
    public boolean isPubliclyVisible(Post post) {
        if (post.isHidden()) return false;
        if (!categoryService.hasReadCategoryAuth(post.getCategory(), Role.GUEST)) return false;
        return true;
    }
}
