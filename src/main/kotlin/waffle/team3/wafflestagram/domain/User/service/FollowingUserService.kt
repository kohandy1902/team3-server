package waffle.team3.wafflestagram.domain.User.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team3.wafflestagram.domain.User.model.FollowingUser
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.FollowingUserRepository

@Service
class FollowingUserService(
    private val followingUserRepository: FollowingUserRepository,
) {
    @Transactional
    fun addFollowing(user: User, following: User) {
        val followingUser = FollowingUser(following)
        user.following.add(followingUser)
    }
}
