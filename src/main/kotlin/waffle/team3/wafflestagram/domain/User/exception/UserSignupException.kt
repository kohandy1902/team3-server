package waffle.team3.wafflestagram.domain.User.exception

import waffle.team3.wafflestagram.global.common.exception.ConflictException
import waffle.team3.wafflestagram.global.common.exception.ErrorType

class UserSignupException(detail: String) :
    ConflictException(ErrorType.USER_ALREADY_EXISTS, detail)
