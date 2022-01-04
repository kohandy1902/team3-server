package waffle.team3.wafflestagram.domain.Comment.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Reply.model.Reply
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "comments")
class Comment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "id", nullable = true)
    val feed: Feed,

    val writer: String,
    var text: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "comment")
    val replies: MutableList<Reply> = mutableListOf(),
) : BaseTimeEntity()
