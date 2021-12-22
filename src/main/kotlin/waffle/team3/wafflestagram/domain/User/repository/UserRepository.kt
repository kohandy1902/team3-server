package waffle.team3.wafflestagram.domain.User.repository

import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.User.model.User

interface UserRepository : JpaRepository<User, Long?> {
    fun findByEmail(email: String): User?
}
