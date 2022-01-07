package waffle.team3.wafflestagram.domain.User.exception

import waffle.team3.wafflestagram.global.common.exception.DataNotFoundException
import waffle.team3.wafflestagram.global.common.exception.ErrorType

class FollowingUserDoesNotExistException(detail : String = "") :
    DataNotFoundException(ErrorType.FOLLOWING_USER_NOT_FOUND, detail)
