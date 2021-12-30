package waffle.team3.wafflestagram.domain.User.exception

import waffle.team3.wafflestagram.global.common.exception.DataNotFoundException
import waffle.team3.wafflestagram.global.common.exception.ErrorType

class UserDoesNotExistException(detail: String) :
    DataNotFoundException(ErrorType.USER_NOT_FOUND, detail)
