package waffle.team3.wafflestagram.domain.User.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.model.WaitingFollowerUser
import waffle.team3.wafflestagram.domain.User.repository.WaitingFollowerUserRepository

@Service
class WaitingFollowerUserService(
    private val waitingFollowerUserRepository: WaitingFollowerUserRepository,
) {
    @Transactional
    fun addWaitingFollower(user: User, waitingFollower: User) {
        val waitingFollowerUser = WaitingFollowerUser(waitingFollower)
        user.waitingFollower.add(waitingFollowerUser)
    }
}
