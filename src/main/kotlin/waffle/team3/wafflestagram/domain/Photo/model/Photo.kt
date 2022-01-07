package waffle.team3.wafflestagram.domain.Photo.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "photo_table")
class Photo(
    @Column(name = "photo_key")
    val key: String,
    @Column(name = "photo_url")
    val url: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "id")
    val feed: Feed
) : BaseTimeEntity()
