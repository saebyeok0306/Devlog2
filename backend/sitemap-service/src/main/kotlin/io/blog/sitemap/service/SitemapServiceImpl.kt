package io.blog.sitemap.service

import com.fasterxml.jackson.databind.util.JSONPObject
import io.blog.sitemap.dto.PostUrlDto
import org.apache.tomcat.util.json.JSONParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class SitemapServiceImpl(
        @Value("\${sitemap.path}") private val sitemapPath: String,
        @Value("\${sitemap.resource.path}") private val sitemapResourcePath: String,
        private val mainService: MainService
) : SitemapService {

    val siteUrl : String = "https://devlog.run"

    override fun generateSitemap(): String {
        val sb = StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n\n")
        sb.append("<url><loc>${siteUrl}</loc></url>\n")

        val location = this.getFolder(sitemapResourcePath)
        for (subSitemap in location.toFile().listFiles()!!) {
            for (line in subSitemap.readLines()) {
                sb.append("<url><loc>$line</loc></url>\n")
            }
        }
        sb.append("</urlset>")
        return sb.toString()
    }

    override fun generateSitemapXml(sitemap: String) {
        val location: Path = this.getFolder(sitemapPath)
        val sitemapFile: Path = this.getSitemap(location, "sitemap.xml")

        Files.write(sitemapFile, sitemap.toByteArray(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
    }

    override fun addPostToSubSitemap(postUrlDto: PostUrlDto) {
        val location = this.getFolder(sitemapResourcePath)
        val subSitemapFile = this.getSitemap(location, "sub_sitemap_${postUrlDto.categoryId}.xml")

        val url = this.getSitemapUrl(postUrlDto.url)
        var flag = true
        for (line in Files.readAllLines(subSitemapFile)) {
            if (line.equals(url)) {
                flag = false
                break
            }
        }
        if (flag) Files.write(subSitemapFile, "$url\n".toByteArray(), StandardOpenOption.APPEND)

        val generateSitemap = this.generateSitemap()
        this.generateSitemapXml(generateSitemap)
    }

    override fun deletePostFromSubSitemap(postUrlDto: PostUrlDto) {
        val location = this.getFolder(sitemapResourcePath)
        val subSitemapFile = this.getSitemap(location, "sub_sitemap_${postUrlDto.categoryId}.xml")

        val url = this.getSitemapUrl(postUrlDto.url)

        Files.newBufferedReader(subSitemapFile).use { reader ->
            val lines : List<String> = reader.readLines()
            val newLines = lines.filter { it != url }

            println("Lines: $newLines")
            println("New Lines: $newLines")

            if (newLines.isEmpty()) {
                Files.delete(subSitemapFile)
            } else {
                Files.write(subSitemapFile, newLines)
            }
        }

        val generateSitemap = this.generateSitemap()
        this.generateSitemapXml(generateSitemap)
    }

    override fun createAllPostToSubSitemap() {
        val allPosts = mainService.getAllPosts()

        for (post in allPosts.posts) {
            val postUrlDto = PostUrlDto(post.category.id, post.url)
            this.addPostToSubSitemap(postUrlDto)
        }
    }

    fun getFolder(location: String) : Path {
        val path = Paths.get(location)
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
        return path
    }

    fun getSitemap(directoryPath : Path, filename: String) : Path {
        val subSitemapFile = directoryPath.resolve(filename)

        if (!Files.exists(subSitemapFile)) {
            Files.createFile(subSitemapFile)
        }
        return subSitemapFile
    }

    fun getSitemapUrl(url: String) : String {
        return "${siteUrl}/post/$url"
    }
}