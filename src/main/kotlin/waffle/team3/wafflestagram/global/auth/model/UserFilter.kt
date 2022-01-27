package waffle.team3.wafflestagram.global.auth.model

import waffle.team3.wafflestagram.domain.User.model.SignupType

class UserFilter(
    val email: String,
    val signupType: SignupType,
)
