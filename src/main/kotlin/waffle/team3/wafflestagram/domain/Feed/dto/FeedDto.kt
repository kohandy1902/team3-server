package waffle.team3.wafflestagram.domain.Feed.dto

class FeedDto {
    data class CreateRequest(
        val photoPath: String,
        val content: String
    )
}