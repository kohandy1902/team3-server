package waffle.team3.wafflestagram.domain.Feed.model

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "feeds")
class Feed(
    @Column
    var photoKeys: String = "",
    @Column
    var content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @Column(name = "comments")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feed")
    var comments: MutableList<Comment> = mutableListOf(),
) : BaseTimeEntity()
