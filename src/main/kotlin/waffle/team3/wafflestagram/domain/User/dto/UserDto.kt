package waffle.team3.wafflestagram.domain.User.dto

import waffle.team3.wafflestagram.domain.User.model.User
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val email: String,
    ) {
        constructor(user: User) : this(
            email = user.email,
        )
    }
    data class SignupRequest(
        @field:NotBlank
        val email: String,

        @field:NotBlank
        val password: String,
    )
}