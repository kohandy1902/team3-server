package waffle.team3.wafflestagram.domain.Reply.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Reply.model.Reply

interface ReplyRepository : JpaRepository<Reply, Long?> {
    fun findByComment(pageable: Pageable, comment: Comment): Page<Reply>?
}
