package waffle.team3.wafflestagram.domain.Feed.dto

import waffle.team3.wafflestagram.domain.User.model.User

class FeedDto {
    data class CreateRequest(
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