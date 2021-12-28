package waffle.team3.wafflestagram.domain.Feed.service

import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Feed.dto.FeedDto
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Feed.repository.FeedRepository
import waffle.team3.wafflestagram.domain.User.model.User

@Service
class FeedService(
    private val feedRepository: FeedRepository
) {

    fun create(createRequest: FeedDto.CreateRequest, user: User): Feed {
        val feed = Feed(createRequest.photoPath, createRequest.content)

    }
}