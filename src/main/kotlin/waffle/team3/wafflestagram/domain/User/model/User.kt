package waffle.team3.wafflestagram.domain.User.model

import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    val email: String,
    val password: String? = null,
    var public: Boolean = true,
    val signupType: SignupType,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var follower: MutableSet<FollowerUser> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var following: MutableSet<FollowingUser> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var waitingFollower: MutableSet<WaitingFollowerUser> = mutableSetOf(),

    var name: String? = null,
    var nickname: String? = null,
    var website: String? = null,
    var bio: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    var feeds: MutableList<Feed> = mutableListOf(),
    var birthday: String? = null,
    var phoneNumber: String? = null,

    var profilePhotoKey: String? = null,
    var profilePhotoURL: String,
) : BaseTimeEntity()

enum class SignupType {
    APP, GOOGLE, FACEBOOK
}
