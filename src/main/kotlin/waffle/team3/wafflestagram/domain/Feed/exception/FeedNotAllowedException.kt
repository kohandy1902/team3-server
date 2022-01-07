package waffle.team3.wafflestagram.domain.Feed.exception

import waffle.team3.wafflestagram.global.common.exception.ErrorType
import waffle.team3.wafflestagram.global.common.exception.NotAllowedException

class FeedNotAllowedException(detail : String = "") :
    NotAllowedException(ErrorType.FEED_NOT_ALLOWED, detail)
