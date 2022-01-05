package waffle.team3.wafflestagram.domain.UserTag.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "user_tags")
class UserTag(
    @ManyToOne(fetch = FetchType.LAZY)
    val feed: Feed,

    @OneToOne(fetch = FetchType.LAZY)
    val user: User
) : BaseTimeEntity()
