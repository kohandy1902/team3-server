package waffle.team3.wafflestagram.domain.Feed.model

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Photo.model.Photo
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Feed(
    var content: String,

    @OneToMany
    val tags: MutableList<User> = mutableListOf(),

    @OneToMany
    var comments: MutableList<Comment> = mutableListOf(),
    @OneToMany
    var likes: MutableList<User> = mutableListOf(),
) : BaseTimeEntity()
