package waffle.team3.wafflestagram.domain.Reply.model

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "replies")
class Reply(
    val writer: String,
    var text: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    val comment: Comment,
) : BaseTimeEntity()
