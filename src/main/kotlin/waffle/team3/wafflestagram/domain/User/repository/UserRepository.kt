package waffle.team3.wafflestagram.domain.User.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.User.model.User

interface UserRepository : JpaRepository<User, Long?> {
    fun findByEmail(email: String): User?
    fun findByNickname(nickname: String): User?
    fun findByNicknameStartsWith(nickname_prefix: String, pageable: Pageable): Page<User>
}
