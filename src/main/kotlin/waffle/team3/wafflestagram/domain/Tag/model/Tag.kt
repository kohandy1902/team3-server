package waffle.team3.wafflestagram.domain.Tag.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tags")
class Tag(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "id")
    val feed: Feed,

    @Column
    var content: String
) : BaseTimeEntity()
