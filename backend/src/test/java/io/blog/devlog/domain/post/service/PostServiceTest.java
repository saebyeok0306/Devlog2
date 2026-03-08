package io.blog.devlog.domain.post.service;

import io.blog.devlog.config.TestConfig;
import io.blog.devlog.domain.category.model.Category;
import io.blog.devlog.domain.file.service.FileService;
import io.blog.devlog.domain.post.dto.ResponsePostUrlDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.post.model.PostDetail;
import io.blog.devlog.domain.post.repository.PostRepository;
import io.blog.devlog.domain.user.model.Role;
import io.blog.devlog.domain.user.service.UserService;
import io.blog.devlog.global.client.SitemapClient;
import io.blog.devlog.utils.EntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private SitemapClient sitemapClient;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;
    @Mock
    private FileService fileService;
    @InjectMocks
    private PostService postService;
    private static final TestConfig testConfig = new TestConfig();

    @BeforeEach
    void setUp() {
        testConfig.clearAuthentication();
    }


    @Test
    @DisplayName("게시글 ID 기반 조회 테스트")
    void getPostById() {
        // given
        Post post = Post.builder().build();
        given(postRepository.findById(1L)).willReturn(Optional.ofNullable(post));

        // when
        Post postById = postService.getPostById(1L);

        // then
        assertThat(postById).isEqualTo(post);
    }

    @Test
    @DisplayName("게시글 URL 기반 조회 테스트")
    void getSimplePostByUrl() {
        // given
        Post post = Post.builder().build();
        given(postRepository.findByUrl("url")).willReturn(Optional.ofNullable(post));

        // when
        Post postByUrl = postService.getSimplePostByUrl("url");

        // then
        assertThat(postByUrl).isEqualTo(post);
    }

    @Test
    @DisplayName("게시글 URL 기반 유저 조회 테스트")
    void getPostByUrl() {
        // given
        testConfig.updateAuthentication(testConfig.adminUser);
        Category category = Category.builder().writeCommentAuth(Role.ADMIN).build();
        Post post = Post.builder().category(category).build();
        given(userService.getUserByEmail(testConfig.adminUser.getEmail())).willReturn(Optional.ofNullable(testConfig.adminUser));
        given(postRepository.findPostByUrl("url", 0L, true, testConfig.adminUser.getRole())).willReturn(Optional.ofNullable(post));

        // when
        PostDetail postByUrl = postService.getPostByUrl("url");

        // then
        assertThat(postByUrl.getPost()).isEqualTo(post);
    }

    @Test
    @DisplayName("GUEST 모든 게시글 조회 테스트")
    void getPosts() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Post post = Post.builder().build();
        Page<Post> posts = new PageImpl<>(List.of(post));

        given(postRepository.findAllPagePublicPosts(pageRequest, Role.GUEST)).willReturn(posts);

        // when
        Page<Post> getPosts = postService.getPosts(pageRequest);

        // then
        assertThat(getPosts).isEqualTo(posts);
    }

    @Test
    @DisplayName("GUEST가 아닌 유저의 모든 게시글 조회 테스트")
    void getPosts2() {
        // given
        testConfig.updateAuthentication(testConfig.adminUser);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Post post = Post.builder().build();
        Page<Post> posts = new PageImpl<>(List.of(post));

        given(userService.getUserByEmail(testConfig.adminUser.getEmail())).willReturn(Optional.ofNullable(testConfig.adminUser));
        given(postRepository.findAllPageUserPosts(pageRequest, null, true, Role.ADMIN)).willReturn(posts);

        // when
        Page<Post> getPosts = postService.getPosts(pageRequest);

        // then
        assertThat(getPosts).isEqualTo(posts);
    }

    @Test
    void getPostsByCategory() {
        // given
        testConfig.updateAuthentication(testConfig.adminUser);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Post post = Post.builder().build();
        Page<Post> posts = new PageImpl<>(List.of(post));

        given(userService.getUserByEmail(testConfig.adminUser.getEmail())).willReturn(Optional.ofNullable(testConfig.adminUser));
        given(postRepository.findAllPageByCategory(pageRequest, "카테고리", null, true, Role.ADMIN)).willReturn(posts);

        // when
        Page<Post> getPosts = postService.getPostsByCategory("카테고리", pageRequest);

        // then
        assertThat(getPosts).isEqualTo(posts);
    }

    @Test
    void getPostsByCategoryId() {
        // given
        testConfig.updateAuthentication(testConfig.adminUser);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Post post = Post.builder().build();
        Page<Post> posts = new PageImpl<>(List.of(post));

        given(userService.getUserByEmail(testConfig.adminUser.getEmail())).willReturn(Optional.ofNullable(testConfig.adminUser));
        given(postRepository.findAllPageByCategoryId(pageRequest, 1L, null, true, Role.ADMIN)).willReturn(posts);


        // when
        Page<Post> getPosts = postService.getPostsByCategoryId(1L, pageRequest);

        // then
        assertThat(getPosts).isEqualTo(posts);
    }

    @Test
    void getAllPostsByCategoryId() {
        // given
        testConfig.updateAuthentication(testConfig.adminUser);
        Post post = Post.builder().build();
        List<Post> posts = List.of(post);

        given(postRepository.findAllByCategoryId(1L)).willReturn(List.of(post));


        // when
        List<Post> getPosts = postService.getAllPostsByCategoryId(1L);

        // then
        assertThat(getPosts).isEqualTo(posts);
    }

    @Test
    void deletePost() {
        // given
        Post post = EntityFactory.createPost();
        ResponsePostUrlDto responsePostUrlDto = ResponsePostUrlDto.of(post.getCategory().getId(), post.getUrl());
        doAnswer(invocation -> {System.out.println("deleteFileFromPost"); return null;}).when(fileService).deleteFileFromPost(post);
        doAnswer(invocation -> {System.out.println("delete"); return null;}).when(postRepository).delete(post);

        // when
        postService.deletePost(post);

        // then
        verify(fileService).deleteFileFromPost(post);
        verify(postRepository).delete(post);
        ArgumentCaptor<ResponsePostUrlDto> captor = ArgumentCaptor.forClass(ResponsePostUrlDto.class);
        verify(sitemapClient).deletePostSitemap(captor.capture());

        ResponsePostUrlDto capturedDto = captor.getValue();
        assertThat(capturedDto).isNotNull();
        assertThat(capturedDto.getUrl()).isEqualTo(responsePostUrlDto.getUrl());
    }

    @Test
    void getInfinitePosts() {
        // given
        testConfig.updateAuthentication(testConfig.adminUser);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Post post = Post.builder().build();
        Slice<Post> posts = new SliceImpl<>(List.of(post));

        given(userService.getUserByEmail(testConfig.adminUser.getEmail())).willReturn(Optional.ofNullable(testConfig.adminUser));
        given(postRepository.findAllSlicePageUserPosts(pageRequest,  0L, null, true, Role.ADMIN)).willReturn(posts);

        // when
        Slice<Post> getPosts = postService.getInfinitePosts(pageRequest, 0L);

        // then
        assertThat(getPosts).isEqualTo(posts);
    }
}
