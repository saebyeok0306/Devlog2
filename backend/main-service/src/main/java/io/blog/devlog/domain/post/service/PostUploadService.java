package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.category.service.CategoryService;
import io.blog.devlog.domain.file.service.FileService;
import io.blog.devlog.domain.post.dto.RequestEditPostDto;
import io.blog.devlog.domain.post.dto.RequestPostDto;
import io.blog.devlog.domain.post.dto.ResponsePostUrlDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.repository.PostRepository;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.client.SitemapClient;
import io.blog.devlog.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostUploadService {
    private final SitemapClient sitemapClient;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final PublicPostService publicPostService;

    public Post savePost(RequestPostDto requestPostDto) {
        String email = getUserEmail();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found : " + email));
        Category category = categoryService.getCategoryById(requestPostDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found : " + requestPostDto.getCategoryId()));

        Post post = postRepository.save(requestPostDto.toEntity(user, category));
        categoryService.updateNewPostDatetime(category);

        fileService.uploadFileAndDeleteTempFile(post, requestPostDto.getFiles());
        fileService.deleteTempFiles(); // 임시파일 제거
        if (publicPostService.isPubliclyVisible(post)) {
            sitemapClient.addPostSitemap(ResponsePostUrlDto.of(requestPostDto.getCategoryId(), requestPostDto.getUrl()));
        }
        return post;
    }

    public Post editPost(Post prevPost, RequestEditPostDto requestEditPostDto) {
        // INFO: Spring JPA에서 같은 id를 가진 Entity는 서로 다른 상황에서 가져온 객체여도 주소가 같음. (영속성 컨텍스트 Persistence Context)
        String prevPostUrl = prevPost.getUrl();
        Long prevCategoryId = prevPost.getCategory().getId();

        String email = getUserEmail();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found : " + email));
        Category category = categoryService.getCategoryById(requestEditPostDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found : " + requestEditPostDto.getCategoryId()));

        Post post = postRepository.save(requestEditPostDto.toEntity(user, category));
        fileService.uploadFileAndDeleteTempFile(post, requestEditPostDto.getFiles());
        fileService.deleteTempFiles(); // 임시파일 제거
        fileService.deleteUnusedFilesByPost(post, requestEditPostDto.getFiles());

        if (!prevPostUrl.equals(post.getUrl()) || !prevCategoryId.equals(post.getCategory().getId())) {
            sitemapClient.deletePostSitemap(ResponsePostUrlDto.of(prevCategoryId, prevPostUrl));
            if (publicPostService.isPubliclyVisible(post)) {
                sitemapClient.addPostSitemap(ResponsePostUrlDto.of(post.getCategory().getId(), post.getUrl()));
            }
        }
        return post;
    }
}
