package waffle.team3.wafflestagram.global.auth.model

import waffle.team3.wafflestagram.domain.User.model.SignupType

class UserFilter(
    val email: String,
    val signupType: SignupType,
) {
    override fun toString(): String {
        return email + " " + signupType.ordinal
    }

    companion object {
        fun parseUserFilter(s: String): UserFilter? {
            val email = s.substring(0, s.length - 2)
            val signupTypeInt = s[s.length - 1] - '0'
            for (type in SignupType.values()) {
                if (type.ordinal == signupTypeInt) return UserFilter(email, type)
            }
            return null
        }
    }
}
