package waffle.team3.wafflestagram.domain.User.dto

import waffle.team3.wafflestagram.domain.User.model.FollowerUser
import java.time.LocalDateTime

class FollowerUserDto {
    data class Response(
        val id: Long,
        val user: UserDto.Response,
        val createdAt: LocalDateTime?
    ) {
        constructor(followerUser: FollowerUser) : this(
            id = followerUser.id,
            user = UserDto.Response(followerUser.user),
            createdAt = followerUser.createdAt,
        )
    }
}
