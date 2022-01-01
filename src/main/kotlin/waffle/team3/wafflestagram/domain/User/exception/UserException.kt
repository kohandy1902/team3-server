package waffle.team3.wafflestagram.domain.User.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.InvalidRequestException

class UserException(detail: String) :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)
