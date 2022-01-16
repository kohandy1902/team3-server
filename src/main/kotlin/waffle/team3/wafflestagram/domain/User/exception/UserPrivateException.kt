package waffle.team3.wafflestagram.domain.User.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.NotAllowedException

class UserPrivateException(detail: String = "") :
    NotAllowedException(ErrorType.PRIVATE_NOT_ALLOWED, detail)
