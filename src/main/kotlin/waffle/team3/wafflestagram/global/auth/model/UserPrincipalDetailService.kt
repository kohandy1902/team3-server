package waffle.team3.wafflestagram.global.auth.model

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.model.SignupType
import waffle.team3.wafflestagram.domain.User.repository.UserRepository

@Service
class UserPrincipalDetailService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(s: String): UserDetails {
        val userFilter = UserFilter.parseUserFilter(s) ?: throw UsernameNotFoundException("Parse Error")
        val user = userRepository.findByEmailAndSignupType(userFilter.email, userFilter.signupType)
            ?: throw UsernameNotFoundException("User with ${userFilter.email} and ${userFilter.signupType} not found")
        return UserPrincipal(user)
    }
}
