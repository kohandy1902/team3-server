package waffle.team3.wafflestagram.domain.Reply.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team3.wafflestagram.domain.Comment.exception.InvalidCommentException
import waffle.team3.wafflestagram.domain.Comment.repository.CommentRepository
import waffle.team3.wafflestagram.domain.Reply.dto.ReplyDto
import waffle.team3.wafflestagram.domain.Reply.exception.InvalidReplyException
import waffle.team3.wafflestagram.domain.Reply.model.Reply
import waffle.team3.wafflestagram.domain.Reply.repository.ReplyRepository
import waffle.team3.wafflestagram.domain.User.model.User

@Service
class ReplyService(
    private val replyRepository: ReplyRepository,
    private val commentRepository: CommentRepository,
) {
    @Transactional
    fun create(createRequest: ReplyDto.CreateRequest, user: User, commentId: Long): Reply {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw InvalidCommentException("NO corresponding comment")
        return replyRepository.save(
            Reply(
                writer = user,
                text = createRequest.text,
                comment = comment,
            )
        )
    }

    fun get(id: Long): Reply {
        return replyRepository.findByIdOrNull(id) ?: throw InvalidReplyException("No corresponding reply")
    }

    @Transactional
    fun getList(commentId: Long, pageable: Pageable): Page<Reply> {
        val comment = commentRepository.findByIdOrNull(commentId)!!
        return replyRepository.findByComment(pageable, comment) ?: Page.empty()
    }

    @Transactional
    fun update(updateRequest: ReplyDto.UpdateRequest, id: Long): Reply {
        val reply = replyRepository.findByIdOrNull(id) ?: throw InvalidReplyException("No corresponding reply")
        reply.text = updateRequest.text
        return replyRepository.save(reply)
    }

    @Transactional
    fun delete(id: Long) {
        replyRepository.delete(replyRepository.findByIdOrNull(id) ?: throw InvalidReplyException("No corresponding reply"))
    }
}
