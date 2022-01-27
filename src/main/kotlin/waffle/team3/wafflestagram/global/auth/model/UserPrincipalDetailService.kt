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
        val email = s.substring(0, s.length - 2)
        val signupTypeInt = s[s.length - 1] - '0'
        var signupType: SignupType = SignupType.APP
        for (type in SignupType.values()) {
            if (type.ordinal == signupTypeInt) signupType = type
        }
        val user = userRepository.findByEmailAndSignupType(email, signupType)
            ?: throw UsernameNotFoundException("User with $email and $signupType not found")
        return UserPrincipal(user)
    }
}
