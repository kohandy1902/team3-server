package waffle.team3.wafflestagram.domain.User.exception

import waffle.team3.wafflestagram.global.common.exception.DataNotFoundException
import waffle.team3.wafflestagram.global.common.exception.ErrorType

class UserException(detail: String) :
    DataNotFoundException(ErrorType.INVALID_REQUEST, detail)
