package waffle.team3.wafflestagram.domain.User.dto

import waffle.team3.wafflestagram.domain.User.model.FollowingUser
import java.time.LocalDateTime

class FollowingUserDto {
    data class Response(
        val id: Long,
        val user: UserDto.Response,
        val createdAt: LocalDateTime?
    ) {
        constructor(followingUser: FollowingUser) : this(
            id = followingUser.id,
            user = UserDto.Response(followingUser.user),
            createdAt = followingUser.createdAt,
        )
    }
}
