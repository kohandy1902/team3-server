package waffle.team3.wafflestagram.domain.User.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signup(signupRequest: UserDto.SignupRequest): User {
        return userRepository.save(
            User(email = signupRequest.email,
                password = passwordEncoder.encode(signupRequest.password)
                )
        )
    }
}