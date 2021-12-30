package waffle.team3.wafflestagram.domain.Comment.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.InvalidRequestException

class InvalidCommentException(detail: String = "") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)
