package waffle.team3.wafflestagram.domain.User.dto

import waffle.team3.wafflestagram.domain.User.model.WaitingFollowerUser
import java.time.LocalDateTime

class WaitingFollowerUserDto {
    data class Response(
        val id: Long,
        val user: UserDto.Response,
        val createdAt: LocalDateTime?
    ) {
        constructor(waitingFollowerUser: WaitingFollowerUser) : this(
            id = waitingFollowerUser.id,
            user = UserDto.Response(waitingFollowerUser.user),
            createdAt = waitingFollowerUser.createdAt,
        )
    }
}
