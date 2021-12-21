package waffle.team3.wafflestagram.global.oauth.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.InvalidRequestException

class InvalidArgException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)