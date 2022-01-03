package waffle.team3.wafflestagram.domain.User.model

import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "waiting_follower_users")
class WaitingFollowerUser(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
) : BaseTimeEntity()
