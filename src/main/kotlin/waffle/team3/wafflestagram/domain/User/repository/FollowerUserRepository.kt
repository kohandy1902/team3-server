package waffle.team3.wafflestagram.domain.User.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import waffle.team3.wafflestagram.domain.User.model.FollowerUser

interface FollowerUserRepository : JpaRepository<FollowerUser, Long?> {
    fun findByOrderByCreatedAtDesc(pageable: Pageable): Page<FollowerUser>
}
