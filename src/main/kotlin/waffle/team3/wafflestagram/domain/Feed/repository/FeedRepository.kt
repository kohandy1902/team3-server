package waffle.team3.wafflestagram.domain.Feed.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.Feed.model.Feed

interface FeedRepository : JpaRepository<Feed, Long>
