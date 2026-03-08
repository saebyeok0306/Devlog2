package io.blog.sitemap.dto

data class ResponsePostsDto(
        val posts: ArrayList<PostDto>,
        val totalPages: Int,
        val currentPage: Int,
        val totalElements: Long
)
