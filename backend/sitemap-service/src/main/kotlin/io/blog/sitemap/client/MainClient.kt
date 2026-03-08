package io.blog.sitemap.client

import io.blog.sitemap.dto.ResponsePostsDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "main-service", )
interface MainClient {
    @GetMapping("/posts?page={page}&size={size}")
    fun getPosts(@PathVariable("page") page: Int, @PathVariable("size") size: Int) : ResponsePostsDto
}