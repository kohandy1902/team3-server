package waffle.team3.wafflestagram.domain.Feed.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.Feed.dto.FeedDto
import waffle.team3.wafflestagram.domain.Feed.exception.FeedDoesNotExistException
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Feed.repository.FeedRepository
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
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
    fun upload(uploadRequest: FeedDto.UploadRequest, user: User): Feed {
        val tags: MutableList<User> = mutableListOf()

        for (nickname: String in uploadRequest.tags) {
            val user = userRepository.findByNickname(nickname) ?: throw UserDoesNotExistException("User with this nickname does not exist.")
            tags.add(user)
        }

        return feedRepository.save(
            Feed(uploadRequest.content, tags)
        )
    }

    fun update(id: Long, updateRequest: FeedDto.UpdateRequest, user: User): Feed {
        val feed = feedRepository.findByIdOrNull(id) ?: throw FeedDoesNotExistException("Feed with this key does not exist.")
        val tags: MutableList<User> = mutableListOf()

        for (nickname: String in updateRequest.tags) {
            val user = userRepository.findByNickname(nickname) ?: throw UserDoesNotExistException("User with this nickname does not exist.")
            tags.add(user)
        }

        return feed.apply {
            updateRequest.content.let { content = it }
            tags.let { this.tags = it }
        }
    }

    fun get(id: Long): Feed {
        return feedRepository.findByIdOrNull(id) ?: throw FeedDoesNotExistException("Feed with this key does not exist.")
    }

    fun delete(id: Long, user: User) {
        val feed = get(id)
        feedRepository.delete(feed)
    }
}
