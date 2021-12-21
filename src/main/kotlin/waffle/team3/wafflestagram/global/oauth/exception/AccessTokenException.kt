package waffle.team3.wafflestagram.global.oauth.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.InvalidRequestException

class AccessTokenException(detail: String = "") :
    InvalidRequestException(ErrorType.SERVER_ERROR, detail)