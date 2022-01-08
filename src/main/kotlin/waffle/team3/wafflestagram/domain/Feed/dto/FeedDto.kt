package waffle.team3.wafflestagram.domain.Feed.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import waffle.team3.wafflestagram.domain.Comment.dto.CommentDto
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Photo.dto.PhotoDto
import waffle.team3.wafflestagram.domain.Tag.dto.TagDto
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.UserTag.dto.UserTagDto
import java.time.LocalDateTime

class FeedDto {
    data class Response(
        val id: Long,
        val author: UserDto.Response,
        val photos: List<PhotoDto.Response>,
        val content: String,
        val comments: List<CommentDto.Response>,
        val likes: List<UserDto.Response>,
        val likeSum: Int,
        val tags: List<TagDto.Response>,
        @JsonProperty("user_tags")
        val userTags: List<UserTagDto.Response>,
        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        val createdAt: LocalDateTime?, //  null 이 아니어도 되지 않을까?
        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        val updatedAt: LocalDateTime?
    ) {
        constructor(feed: Feed) : this(
            id = feed.id,
            author = UserDto.Response(feed.user),
            photos = feed.photos.let { it.map { photo -> PhotoDto.Response(photo) } },
            content = feed.content,
            comments = feed.comments.let { it.map { comment -> CommentDto.Response(comment) } },
            likes = feed.likes.let { it.map { like -> UserDto.Response(like.user) } },
            likeSum = feed.likes.count(),
            tags = feed.tags.let { it.map { tag -> TagDto.Response(tag.content) } },
            userTags = feed.userTags.let { it.map { userTag -> UserTagDto.Response(userTag.user.nickname) } },
            createdAt = feed.createdAt,
            updatedAt = feed.updatedAt
        )
    }

    data class UploadRequest(
        val content: String,
        val imageKeys: List<String>,
        val tags: List<String>,
        @JsonProperty("user_tags")
        val userTags: List<String>
    )

    data class UpdateRequest(
        val content: String,
        val imageKeys: List<String>,
        val tags: List<String>,
        @JsonProperty("user_tags")
        val userTags: List<String>
    )
}
