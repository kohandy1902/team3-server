package waffle.team3.wafflestagram.domain.User.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Photo.model.Photo
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class User(
    @Column(unique = true)
    val email: String,
    val password: String? = null,
    var public: Boolean = true,
    @OneToMany
    var follower: MutableList<User> = mutableListOf(),
    @OneToMany
    var following: MutableList<User> = mutableListOf(),
    @OneToMany
    var waitingFollower: MutableList<User> = mutableListOf(),
    var name: String? = null,
    var nickname: String? = null,
    var website: String? = null,
    var bio: String? = null,
    @OneToOne
    var profilePhoto: Photo? = null,
    @OneToMany
    var feeds: MutableList<Feed> = mutableListOf(),
) : BaseTimeEntity()
