package waffle.team3.wafflestagram.global.oauth.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.InvalidRequestException

class InvalidTokenException(detail: String = "") :
    InvalidRequestException(ErrorType.FACEBOOK_TOKEN_INVALID, detail)
