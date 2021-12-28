package waffle.team3.wafflestagram.domain.Feed.dto

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.User.model.User

class FeedDto {
    data class Response(
        val id: Long,
        val content: String,
        val tags: MutableList<User>,
        val comments: MutableList<Comment>,
        val likes: MutableList<User>,
    ) {
        constructor(feed: Feed): this(
            id = feed.id,
            content = feed.content,
            tags = feed.tags,
            comments = feed.comments,
            likes = feed.likes
        )
    }

    data class UploadRequest(
        val content: String,
        val tags: MutableList<User>
    )

    data class UpdateRequest(
        val content: String,
        val tags: MutableList<User>
    )

    data class DeleteRequest(
        val id: Long
    )
}