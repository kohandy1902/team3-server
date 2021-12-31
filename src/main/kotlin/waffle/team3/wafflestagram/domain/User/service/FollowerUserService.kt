package waffle.team3.wafflestagram.domain.User.service

import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.repository.FollowerUserRepository

@Service
class FollowerUserService(
    private val followerUserRepository: FollowerUserRepository,
) {

}
