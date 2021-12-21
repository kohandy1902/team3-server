package waffle.team3.wafflestagram.domain.Comment.model

import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity

@Entity
class Comment(
    val writer: String,
    val text: String,
) : BaseTimeEntity()
