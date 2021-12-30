package waffle.team3.wafflestagram.domain.Reply.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.InvalidRequestException

class InvalidReplyException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)
