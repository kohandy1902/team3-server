package waffle.team3.wafflestagram.domain.Like.dto

import waffle.team3.wafflestagram.domain.Like.model.Like

class LikeDto {
    data class Response(
        val nickname: String?
    ) {
        constructor(like: Like) : this(
            nickname = like.user.nickname
        )
    }
}
