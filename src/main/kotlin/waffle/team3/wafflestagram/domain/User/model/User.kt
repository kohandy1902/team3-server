package waffle.team3.wafflestagram.domain.User.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Photo.model.Photo
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Column(unique = true)
    val email: String,
    val password: String? = null,
    var public: Boolean = true,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var follower: MutableSet<FollowerUser> = mutableSetOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var following: MutableSet<FollowingUser> = mutableSetOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var waitingFollower: MutableSet<WaitingFollowerUser> = mutableSetOf(),

    var name: String? = null,
    var nickname: String? = null,
    var website: String? = null,
    var bio: String? = null,

    //  photo 의 경우 s3의 key 로 저장될 거 같습니다!
    // @OneToOne(mappedBy = "user")
    // var profilePhoto: Photo? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    var feeds: MutableList<Feed> = mutableListOf(),
) : BaseTimeEntity()
