package waffle.team3.wafflestagram.domain.User.dto

import waffle.team3.wafflestagram.domain.User.model.User
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val email: String,
        val name: String? = null,
        val nickname: String? = null,
        val website: String? = null,
        val bio: String? = null,
    ) {
        constructor(user: User) : this(
            email = user.email,
            name = user.name,
            nickname = user.nickname,
            website = user.website,
            bio = user.bio,
        )
    }
    data class SignupRequest(
        @field:NotBlank
        val email: String,

        @field:NotBlank
        val password: String,
    )
    data class ProfileRequest(
        val public: Boolean? = null,
        val name: String? = null,
        val nickname: String? = null,
        val website: String? = null,
        val bio: String? = null,
    )
}
