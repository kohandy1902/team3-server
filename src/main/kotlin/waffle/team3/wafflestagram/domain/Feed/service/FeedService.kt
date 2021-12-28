package waffle.team3.wafflestagram.domain.Feed.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.Feed.dto.FeedDto
import waffle.team3.wafflestagram.domain.Feed.exception.FeedDoesNotExistException
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Feed.repository.FeedRepository
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import javax.transaction.Transactional

@Service
@Transactional
class FeedService(
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository
) {

    // 사람 태그하기, 위치 추가 기능
    fun create(createRequest: FeedDto.CreateRequest, user: User): Feed {
        return feedRepository.save(
            Feed(
                createRequest.content,
                createRequest.tags
            )
        )
    }

    fun update(updateRequest: FeedDto.UpdateRequest, user: User): Feed {
        return feedRepository.save(
            Feed(
                updateRequest.content,
                updateRequest.tags
            )
        )
    }

    fun get(id: Long): Feed {
        return feedRepository.findByIdOrNull(id) ?: throw FeedDoesNotExistException("Feed with this key does not exist.")
    }

    fun delete(deleteRequest: FeedDto.DeleteRequest, user: User) {
        return feedRepository.delete(
            get(deleteRequest.id)
        )
    }


}