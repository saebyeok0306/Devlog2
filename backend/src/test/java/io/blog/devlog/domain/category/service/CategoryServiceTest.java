package io.blog.devlog.domain.category.service;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.category.repository.CategoryRepository;
import io.blog.devlog.domain.post.service.PostService;
import io.blog.devlog.domain.user.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PostService postService;
    @InjectMocks
    private CategoryService categoryService;

    final static int MAX_SIZE = 5;
    final static List<Role> CATEGORY_WRITE_ROLES = List.of(Role.GUEST, Role.GUEST, Role.USER, Role.PARTNER, Role.ADMIN);
    final static List<Role> CATEGORY_READ_ROLES = List.of(Role.GUEST, Role.GUEST, Role.GUEST, Role.USER, Role.ADMIN);
    final static List<Role> UPDATED_CATEGORY_READ_ROLES = List.of(Role.USER, Role.GUEST, Role.GUEST, Role.USER, Role.GUEST);
    public List<Category> createCategories() {
        List<Category> categories = new ArrayList<>();
        for (var i=0; i<MAX_SIZE; i++) {
            Category category = Category.builder()
                    .id((long) i+1)
                    .name(String.format("카테고리 테스트%d", i))
                    .layer(i)
                    .writePostAuth(CATEGORY_WRITE_ROLES.get(i))
                    .readCategoryAuth(CATEGORY_READ_ROLES.get(i))
                    .writeCommentAuth(CATEGORY_READ_ROLES.get(i))
                    .build();
            categories.add(category);
        }
        return categories;
    }

    public List<Category> createUpdatedCategories() {
        List<Category> categories = new ArrayList<>();
        for (var i=0; i<MAX_SIZE; i++) {
            Category category = Category.builder()
                    .id((long) i+1)
                    .name(String.format("카테고리 테스트%d", i))
                    .layer(i)
                    .writePostAuth(CATEGORY_WRITE_ROLES.get(i))
                    .readCategoryAuth(UPDATED_CATEGORY_READ_ROLES.get(i))
                    .writeCommentAuth(UPDATED_CATEGORY_READ_ROLES.get(i))
                    .build();
            categories.add(category);
        }
        return categories;
    }
    
    @Test
    @DisplayName("카테고리id로 가져오기")
    public void getCategoryById() {
        // given
        List<Category> categories = createCategories();
        Category category = categories.get(0);
        Long id = category.getId();
        given(categoryRepository.findById(id)).willReturn(Optional.of(category));

        // when
        Optional<Category> foundCategory = categoryService.getCategoryById(id);

        // then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("카테고리이름으로 가져오기")
    public void getCategoryByName() {
        // given
        List<Category> categories = createCategories();
        Category category = categories.get(0);
        String name = category.getName();
        given(categoryRepository.findByName(name)).willReturn(Optional.of(category));

        // when
        Optional<Category> foundCategory = categoryService.getCategoryByName(name);

        // then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("카테고리 정렬 검증")
    public void sortCategories() {
        // given
        List<Category> categories = createCategories();
        Collections.shuffle(categories);

        // when
        List<Category> sortedCategories = categoryService.sortCategories(categories);

        // then
        Long last_layer = -1L;
        for (Category sortedCategory : sortedCategories) {
            Long layer = sortedCategory.getLayer();
            assertThat(layer).isGreaterThan(last_layer);
            last_layer = layer;
        }
    }

    @Test
    @DisplayName("카테고리 목록 가져오기 (읽기 GUEST)")
    public void getCategories() {
        // given
        List<Category> categories = createCategories();
        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<Category> guest_categories = categoryService.getCategories();

        // then
        assertThat(guest_categories.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리 목록 가져오기 (읽기,쓰기 GUEST)")
    public void getCategoriesReadWrite() {
        // given
        List<Category> categories = createCategories();
        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<Category> categoriesReadWrite = categoryService.getCategoriesReadWrite();

        // then
        assertThat(categoriesReadWrite.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("카테고리 업데이트")
    public void updateCategories() {
        // given
        List<Category> find_categories = createCategories();
        List<Category> update_categories = find_categories.subList(0, 3);
        List<Category> delete_categories = find_categories.subList(3, 5);
        given(categoryRepository.findAll()).willReturn(find_categories);
        given(categoryRepository.saveAll(update_categories)).willReturn(update_categories);

        // when
        List<Category> updatedCategories = categoryService.updateCategories(update_categories);

        // then
        verify(categoryRepository).deleteAll(delete_categories);
        assertThat(updatedCategories.size()).isEqualTo(update_categories.size());
    }

    @Test
    @DisplayName("카테고리 업데이트 (읽기권한 변경)")
    public void updateCategories2() {
        // given
        List<Category> find_categories = createCategories();
        List<Category> update_categories = createUpdatedCategories();
        given(categoryRepository.findAll()).willReturn(find_categories);
        given(categoryRepository.saveAll(update_categories)).willReturn(update_categories);

        // when
        List<Category> updatedCategories = categoryService.updateCategories(update_categories);

        // then
    }

    @Test
    @DisplayName("카테고리 전부 정리")
    public void cleanUpCategories() {
        // given

        // when
        categoryService.cleanUpCategories();

        // then
        verify(categoryRepository).truncate();
    }


    @Test
    @DisplayName("카테고리 읽기 권한 검증 (Category category)")
    void hasReadCategoryAuth() {
        // given
        List<Category> categories = createCategories();
        Category category = categories.get(3); // w,r,c 파트너, 유저, 유저

        // when
        boolean hasReadCategoryAuth = categoryService.hasReadCategoryAuth(category); // GUEST

        // then
        assertThat(hasReadCategoryAuth).isFalse();
    }

    @Test
    @DisplayName("카테고리 읽기 권한 검증 (Category category, Role role)")
    void hasReadCategoryAuth2() {
        // given
        List<Category> categories = createCategories();
        Category category = categories.get(3); // w,r,c 파트너, 유저, 유저

        // when
        boolean hasReadCategoryAuth = categoryService.hasReadCategoryAuth(category, Role.USER);

        // then
        assertThat(hasReadCategoryAuth).isTrue();
    }

    @Test
    @DisplayName("카테고리 읽기,쓰기 권한 검증")
    void hasReadWriteCategoryAuth() {
        // given
        List<Category> categories = createCategories();
        Category category = categories.get(3); // w,r,c 파트너, 유저, 유저

        // when
        boolean userAuth = categoryService.hasReadWriteCategoryAuth(category, Role.USER);
        boolean partnerAuth = categoryService.hasReadWriteCategoryAuth(category, Role.PARTNER);

        // then
        assertThat(userAuth).isFalse();
        assertThat(partnerAuth).isTrue();
    }

    @Test
    @DisplayName("카테고리 댓글쓰기 권한 검증")
    void hasCommentCategoryAuth() {
        // given
        List<Category> categories = createCategories();
        Category category = categories.get(3); // w,r,c 파트너, 유저, 유저

        // when
        boolean guestAuth = categoryService.hasCommentCategoryAuth(category, Role.GUEST);
        boolean partnerAuth = categoryService.hasCommentCategoryAuth(category, Role.PARTNER);

        // then
        assertThat(guestAuth).isFalse();
        assertThat(partnerAuth).isTrue();
    }
}
