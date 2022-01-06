package waffle.team3.wafflestagram.domain.Comment.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team3.wafflestagram.domain.Comment.dto.CommentDto
import waffle.team3.wafflestagram.domain.Comment.exception.InvalidCommentException
import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Comment.repository.CommentRepository
import waffle.team3.wafflestagram.domain.Feed.repository.FeedRepository
import waffle.team3.wafflestagram.domain.User.model.User

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val feedRepository: FeedRepository,
) {
    @Transactional
    fun create(createRequest: CommentDto.CreateRequest, user: User, feedId: Long): Comment {
        val feed = feedRepository.findByIdOrNull(feedId)!!
        return commentRepository.save(
            Comment(
                writer = user,
                text = createRequest.text,
                feed = feed,
            )
        )
    }

    fun get(id: Long): Comment {
        return commentRepository.findByIdOrNull(id) ?: throw InvalidCommentException("No corresponding comment")
    }

    @Transactional
    fun getList(feedId: Long, pageable: Pageable): Page<Comment> {
        val feed = feedRepository.findByIdOrNull(feedId)!!
        return commentRepository.findByFeed(pageable, feed) ?: Page.empty()
    }

    @Transactional
    fun update(updateRequest: CommentDto.UpdateRequest, id: Long): Comment {
        val comment = commentRepository.findByIdOrNull(id) ?: throw InvalidCommentException("No corresponding comment")
        comment.text = updateRequest.text
        return commentRepository.save(comment)
    }

    @Transactional
    fun delete(id: Long) {
        commentRepository.delete(commentRepository.findByIdOrNull(id) ?: throw InvalidCommentException("No corresponding comment"))
    }
}
