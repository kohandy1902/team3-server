package waffle.team3.wafflestagram.domain.Feed.dto

import waffle.team3.wafflestagram.domain.Comment.dto.CommentDto
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import java.time.LocalDateTime

class FeedDto {
    data class Response(
        val id: Long,
        val content: String,
        val comments: List<CommentDto.Response>,
        val createdAt: LocalDateTime?, //  null 이 아니어도 되지 않을까?
        val updatedAt: LocalDateTime?
    ) {
        constructor(feed: Feed) : this(
            id = feed.id,
            content = feed.content,
            comments = feed.comments.let { it.map { comment -> CommentDto.Response(comment) } },
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
