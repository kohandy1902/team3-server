package waffle.team3.wafflestagram.domain.Feed.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.BatchSize
import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Like.model.Like
import waffle.team3.wafflestagram.domain.Tag.model.Tag
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.UserTag.model.UserTag
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.CascadeType
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

    @BatchSize(size = 100)
    @Column(name = "likes")
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "feed")
    var likes: MutableList<Like> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @BatchSize(size = 100)
    @Column(name = "tags")
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "feed")
    var tags: MutableList<Tag> = mutableListOf(),

    @JsonProperty("user_tags")
    @BatchSize(size = 100)
    @Column(name = "user_tags")
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "feed")
    var userTags: MutableList<UserTag> = mutableListOf(),

    @BatchSize(size = 100)
    @Column(name = "comments")
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "feed")
    var comments: MutableList<Comment> = mutableListOf(),
) : BaseTimeEntity()
