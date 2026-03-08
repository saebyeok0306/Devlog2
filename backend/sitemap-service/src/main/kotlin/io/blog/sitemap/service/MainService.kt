package io.blog.sitemap.service

import io.blog.sitemap.dto.ResponsePostsDto

interface MainService {
    fun getAllPosts() : ResponsePostsDto
}