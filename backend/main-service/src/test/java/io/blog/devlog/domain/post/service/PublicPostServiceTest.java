package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.category.service.CategoryService;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.utils.EntityFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.BDDMockito.given;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PublicPostServiceTest {
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private PublicPostServiceImpl publicPostService;

    @Test
    @DisplayName("카테고리 읽기권한이 GUEST인 경우")
    void isPubliclyVisible() {
        // given
        Category category = EntityFactory.createCategory(null, null, null, null, Role.GUEST);
        Post post = EntityFactory.createPost(null, null, null, category);
        given(categoryService.hasReadCategoryAuth(post.getCategory(), Role.GUEST)).willReturn(true);
        // 이런 경우에는 categoryServiceTest 단위테스트에서 테스트하니 문제가 없는 테스트코드인가?

        // when
        boolean visible = publicPostService.isPubliclyVisible(post);

        // then
        Assertions.assertThat(visible).isTrue();
    }

    @Test
    @DisplayName("카테고리 읽기권한이 GUEST가 아닌 경우")
    void isPubliclyVisible2() {
        // given
        Category category = EntityFactory.createCategory(null, null, null, null, Role.USER);
        Post post = EntityFactory.createPost(null, null, null, category);
        given(categoryService.hasReadCategoryAuth(post.getCategory(), Role.GUEST)).willReturn(false);

        // when
        boolean visible = publicPostService.isPubliclyVisible(post);

        // then
        Assertions.assertThat(visible).isFalse();
    }
}
