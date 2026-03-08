package io.blog.sitemap.service

import io.blog.sitemap.dto.PostUrlDto
import java.io.File

interface SitemapService {
    fun generateSitemap() : String
    fun generateSitemapXml(sitemap: String)
    fun addPostToSubSitemap(postUrlDto: PostUrlDto)
    fun deletePostFromSubSitemap(postUrlDto: PostUrlDto)
    fun createAllPostToSubSitemap()
}