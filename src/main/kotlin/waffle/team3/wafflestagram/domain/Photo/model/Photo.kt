package waffle.team3.wafflestagram.domain.Photo.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "photo_table")
class Photo(
    val key: String,
    val url: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "id")
    val feed: Feed
) : BaseTimeEntity()
