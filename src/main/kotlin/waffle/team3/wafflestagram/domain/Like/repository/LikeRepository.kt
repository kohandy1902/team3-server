package waffle.team3.wafflestagram.domain.Like.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import waffle.team3.wafflestagram.domain.Like.model.Like

@Repository
interface LikeRepository : JpaRepository<Like, Long>
