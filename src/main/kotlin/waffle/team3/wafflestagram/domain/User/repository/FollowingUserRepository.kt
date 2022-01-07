package waffle.team3.wafflestagram.domain.User.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.User.model.FollowingUser
import waffle.team3.wafflestagram.domain.User.model.User

interface FollowingUserRepository : JpaRepository<FollowingUser, Long?> {
    fun findByUser(user: User): FollowingUser?
}
