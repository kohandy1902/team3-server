package waffle.team3.wafflestagram.domain.Feed.exception

import waffle.team3.wafflestagram.global.common.exception.DataNotFoundException
import waffle.team3.wafflestagram.global.common.exception.ErrorType

class FeedDoesNotExistException(detail: String = "") :
    DataNotFoundException(ErrorType.FEED_NOT_FOUND, detail)
