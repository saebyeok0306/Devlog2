package io.blog.devlog.domain.category.controller;

import io.blog.devlog.domain.category.dto.CategoryDto;
import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        LocalDateTime now = LocalDateTime.now();
        List<CategoryDto> categories = categoryService.getCategories().stream().map((category) -> CategoryDto.of(category, now)).toList();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getCategoriesDetails() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/readwrite")
    public ResponseEntity<List<CategoryDto>> getCategoriesRW() {
        LocalDateTime now = LocalDateTime.now();
        List<CategoryDto> categories = categoryService.getCategoriesReadWrite().stream().map((category) -> CategoryDto.of(category, now)).toList();
        return ResponseEntity.ok().body(categories);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCategories(@RequestBody List<Category> categories) throws IOException {
        List<Category> sortedCategories = categoryService.sortCategories(categories);
        long idx = sortedCategories.get(0).getLayer();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : sortedCategories) {
            if (categoryNames.contains(category.getName())) {
                throw new BadRequestException("카테고리 이름은 중복될 수 없습니다.");
            }
            categoryNames.add(category.getName());
            if (category.getLayer() == idx) {
                idx ++;
            }
            else {
                throw new BadRequestException("중복된 카테고리 레이어가 존재합니다.");
            }
        }

        categoryService.updateCategories(categories);
    }
}
