package waffle.team3.wafflestagram.domain.User.service

import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.repository.WaitingFollowerUserRepository

@Service
class WaitingFollowerUserService(
    private val waitingFollowerUserRepository: WaitingFollowerUserRepository,
) {

}
