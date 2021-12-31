package waffle.team3.wafflestagram.domain.User.service

import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.repository.FollowingUserRepository

@Service
class FollowingUserService(
    private val followingUserRepository: FollowingUserRepository,
) {

}
