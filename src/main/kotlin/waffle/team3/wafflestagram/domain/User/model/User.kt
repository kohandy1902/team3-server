package waffle.team3.wafflestagram.domain.User.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Photo.model.Photo
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
class User(
    val email: String,
    val password: String,
    var public: Boolean,
    //@OneToMany
    //var follower: MutableList<User> = mutableListOf(),
    //@OneToMany
    //var following: MutableList<User> = mutableListOf(),
    var name: String,
    var nickname: String,
    var website: String,
    var bio: String,
    @OneToOne
    var profilePhoto: Photo,
    @OneToMany
    var feeds: MutableList<Feed> = mutableListOf(),
) : BaseTimeEntity()