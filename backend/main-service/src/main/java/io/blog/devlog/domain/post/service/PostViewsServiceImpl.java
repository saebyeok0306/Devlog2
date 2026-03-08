package io.blog.devlog.domain.post.service;

import io.blog.devlog.domain.post.model.PostViews;
import io.blog.devlog.domain.post.model.PostViewsId;
import io.blog.devlog.domain.post.repository.PostRepository;
import io.blog.devlog.domain.post.repository.PostViewsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostViewsServiceImpl implements PostViewsService {
    private final Long PER_VIEW_COUNT = 6*1000*60*60L; // 6 hour
    private final PostViewsRepository postViewsRepository;
    private final PostRepository postRepository;

    @Override
    public boolean increaseViewCount(Long postId, HttpServletRequest request) {
        String clientIP = request.getRemoteAddr();
        if (request.getHeader("X-Forwarded-For") != null) {
            String forwardForIp = request.getHeader("X-Forwarded-For");
            log.info("X-Forwarded-For : " + forwardForIp);
            clientIP = forwardForIp.split(",")[0];
        }
        String hashIP = DigestUtils.md5Hex(clientIP);

        PostViews postViews = postViewsRepository.findById(new PostViewsId(postId, hashIP)).orElse(null);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (postViews != null && postViews.getViewsAt().getTime() + PER_VIEW_COUNT > now.getTime()) {
            return false;
        }
        if (postViews == null) {
            postViews = PostViews.builder()
                    .id(new PostViewsId(postId, hashIP))
                    .viewsAt(now)
                    .build();
        } else {
            postViews.setViewsAt(now);
        }
        postViewsRepository.save(postViews);
        postRepository.increasePostView(postId);
        return true;
    }
}
