package waffle.team3.wafflestagram.domain.User.dto

import waffle.team3.wafflestagram.domain.User.model.User
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val id: Long,
        val email: String,
        val name: String? = null,
        val nickname: String? = null,
        val birthday: String? = null,
        val public: Boolean = true,
        val website: String? = null,
        val bio: String? = null,
        val profilePhotoURL: String,
    ) {
        constructor(user: User) : this(
            id = user.id,
            email = user.email,
            name = user.name,
            nickname = user.nickname,
            birthday = user.birthday,
            public = user.public,
            website = user.website,
            bio = user.bio,
            profilePhotoURL = user.profilePhotoURL,
        )
    }
    data class SignupRequest(
        @field:NotBlank
        val email: String,

        @field:NotBlank
        val password: String,

        val public: Boolean = true,
        val name: String? = null,
        val nickname: String? = null,
        val birthday: String? = null,
        val phoneNumber: String? = null,
    )
    data class ProfileRequest(
        val public: Boolean? = null,
        val name: String? = null,
        val nickname: String? = null,
        val website: String? = null,
        val bio: String? = null,
        val birthday: String? = null,
        val phoneNumber: String? = null,
    )
    data class ProfilePhotoRequest(
        val profilePhotoKey: String? = null,
    )
    data class ProfilePhotoResponse(
        val profilePhotoURL: String,
    )
}
