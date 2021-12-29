package waffle.team3.wafflestagram.domain.Feed.model

import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Feed(
    var photoKeys: String = "",
    var content: String = "",

    @OneToMany
    var tags: MutableList<User> = mutableListOf(),

    @OneToMany
    var comments: MutableList<Comment> = mutableListOf(),
    @OneToMany
    var likes: MutableList<User> = mutableListOf(),
) : BaseTimeEntity()
