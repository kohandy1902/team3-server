package waffle.team3.wafflestagram.domain.Feed.dto

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import java.time.LocalDateTime

class FeedDto {
    data class Response(
        val content: String,
        val comments: MutableList<Comment>,
        val createdAt: LocalDateTime?, //  null 이 아니어도 되지 않을까?
        val updatedAt: LocalDateTime?
    ) {
        constructor(feed: Feed) : this(
            content = feed.content,
            comments = feed.comments,
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
