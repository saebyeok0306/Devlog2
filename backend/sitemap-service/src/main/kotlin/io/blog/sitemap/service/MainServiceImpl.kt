package io.blog.sitemap.service

import io.blog.sitemap.client.MainClient
import io.blog.sitemap.dto.ResponsePostsDto
import org.springframework.stereotype.Service

@Service
class MainServiceImpl(private val mainClient: MainClient) : MainService {
    override fun getAllPosts() : ResponsePostsDto {
        val allPosts = mainClient.getPosts(0, 5)
        if (allPosts.totalPages > 1) {
            for (i in 1 until allPosts.totalPages) {
                allPosts.posts.addAll(mainClient.getPosts(i, 5).posts)
            }
        }
        return allPosts
    }
}