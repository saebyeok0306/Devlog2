package io.blog.sitemap.controller

import io.blog.sitemap.dto.PostUrlDto
import io.blog.sitemap.service.SitemapService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SitemapController(private val sitemapService: SitemapService) {

    @PostMapping("/sitemap")
    fun generateSitemap() {
        try {
            val generateSitemap = sitemapService.createAllPostToSubSitemap()
            println("Sitemap generated: $generateSitemap")
        } catch (e: Exception) {
            println("Error generating sitemap: $e")
            throw e
        }
    }

    @PostMapping("/sitemap/post")
    fun addPostSitemap(@RequestBody postUrl: PostUrlDto) {
        try {
            sitemapService.addPostToSubSitemap(postUrl)
            println("Post added to sitemap: $postUrl")
        } catch (e: Exception) {
            println("Error adding post to sitemap: $e")
            throw e
        }
    }

    @DeleteMapping("/sitemap/post")
    fun deletePostSitemap(@RequestBody postUrl: PostUrlDto) {
        try {
            sitemapService.deletePostFromSubSitemap(postUrl)
            println("Post deleted to sitemap: $postUrl")
        } catch (e: Exception) {
            println("Error deleting post to sitemap: $e")
            throw e
        }
    }
}