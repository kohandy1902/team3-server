package waffle.team3.wafflestagram.domain.Comment.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Feed.model.Feed

interface CommentRepository : JpaRepository<Comment, Long?> {
    fun findByFeed(pageable: Pageable, feed: Feed): Page<Comment>?
}
