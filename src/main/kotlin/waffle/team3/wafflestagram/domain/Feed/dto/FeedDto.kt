package waffle.team3.wafflestagram.domain.Feed.dto

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.User.model.User
import java.time.LocalDateTime

class FeedDto {
    data class Response(
        val id: Long,
        val content: String,
        val tags: MutableList<User>,
        val comments: MutableList<Comment>,
        val likes: MutableList<User>,
        val createdAt: LocalDateTime,  //  null 이 아니어도 되지 않을까?
        val updatedAt: LocalDateTime
    ) {
        constructor(feed: Feed) : this(
            id = feed.id,
            content = feed.content,
            tags = feed.tags,
            comments = feed.comments,
            likes = feed.likes,
            createdAt = feed.createdAt,
            updatedAt = feed.updatedAt
        )
    }

    data class UploadRequest(
        val content: String,
        val tags: MutableList<String> = mutableListOf()
    )

    data class UpdateRequest(
        val content: String,
        val tags: MutableList<String> = mutableListOf()
    )
}
