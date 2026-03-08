package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.file.service.FileService;
import io.blog.devlog.domain.post.dto.ResponsePostUrlDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.model.PostDetail;
import io.blog.devlog.domain.post.repository.PostRepository;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.model.User;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.client.SitemapClient;
import io.blog.devlog.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.blog.devlog.global.utils.SecurityUtils.getUserEmail;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final SitemapClient sitemapClient;
    private final PostRepository postRepository;
    private final UserService userService;
    private final FileService fileService;


    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post getSimplePostByUrl(String url) {
        return postRepository.findByUrl(url).orElse(null);
    }

    public PostDetail getPostByUrl(String url) {
        String email = getUserEmail();
        return this.getPostByUrl(email, url);
    }

    public PostDetail getPostByUrl(String email, String url) {
        User user = userService.getUserByEmail(email).orElse(null);
        if (user == null) {
            return this.getPostByUrl(url, 0L, false, Role.GUEST);
        }
        return this.getPostByUrl(url, user);
    }

    public PostDetail getPostByUrl(String url, User user) {
        return this.getPostByUrl(url, user.getId() == null ? 0L : user.getId(), user.isAdmin(), user.getRole());
    }

    public PostDetail getPostByUrl(String url, Long userId, boolean isAdmin, Role role) {
        Post post = postRepository.findPostByUrl(url, userId, isAdmin, role).orElseThrow(() -> new NotFoundException("Post not found : " + url));
        return PostDetail.builder()
                .post(post)
                .commentFlag(post.getCategory().getWriteCommentAuth().getKey() <= role.getKey())
                .build();
    }

    public Page<Post> getPosts(Pageable pageable) {
        String email = getUserEmail();
        log.info("getPosts (email: " + email + ")");
        if (email == null) {
            return postRepository.findAllPagePublicPosts(pageable, Role.GUEST);
        }
        User user = userService.getUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found : " + email));
        return postRepository.findAllPageUserPosts(pageable, user.getId(), user.isAdmin(), user.getRole());
    }

    public Page<Post> getPostsByCategory(String categoryName, Pageable pageable) {
        String email = getUserEmail();
        if (email == null) {
            return postRepository.findAllPageByCategory(pageable, categoryName, 0L, false, Role.GUEST);
        }
        User user = userService.getUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found : " + email));
        return postRepository.findAllPageByCategory(pageable, categoryName, user.getId(), user.isAdmin(), user.getRole());
    }

    public Page<Post> getPostsByCategoryId(Long categoryId, Pageable pageable) {
        String email = getUserEmail();
        if (email == null) {
            return postRepository.findAllPageByCategoryId(pageable, categoryId, 0L, false, Role.GUEST);
        }
        User user = userService.getUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found : " + email));
        return postRepository.findAllPageByCategoryId(pageable, categoryId, user.getId(), user.isAdmin(), user.getRole());
    }

    public List<Post> getAllPostsByCategoryId(Long categoryId) {
        return postRepository.findAllByCategoryId(categoryId);

    }

    public void deletePost(Post post) {
        fileService.deleteFileFromPost(post);
        postRepository.delete(post);
        sitemapClient.deletePostSitemap(ResponsePostUrlDto.of(post.getCategory().getId(), post.getUrl()));
    }

    public Slice<Post> getInfinitePosts(Pageable pageable, Long lastId) {
        String email = getUserEmail();
        log.info("getInfinitePosts (email: " + email + ")");
        if (email == null) {
            return postRepository.findAllSlicePagePublicPosts(pageable, lastId, Role.GUEST);
        }
        User user = userService.getUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found : " + email));
        return postRepository.findAllSlicePageUserPosts(pageable, lastId, user.getId(), user.isAdmin(), user.getRole());
    }

    public boolean isPostExists(String url) {
        return postRepository.existsByUrl(url);
    }

    public Category getCategoryByPostId(Long postId) {
        return postRepository.findCategoryByPostId(postId);
    }
}
