package waffle.team3.wafflestagram.domain.User.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserException
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signup(signupRequest: UserDto.SignupRequest): User {
        return userRepository.save(
            User(
                email = signupRequest.email,
                password = passwordEncoder.encode(signupRequest.password)
            )
        )
    }

    fun setProfile(user: User, profileRequest: UserDto.ProfileRequest) {
        val currentUser = userRepository.findByIdOrNull(user.id)!!
        profileRequest.public?.let { currentUser.public = it }
        profileRequest.name?.let { currentUser.name = it }
        profileRequest.nickname?.let {
            if (userRepository.findByNickname(profileRequest.nickname) != null)
                throw UserException("this nickname already exists")
            else currentUser.nickname = it
        }
        profileRequest.website?.let { currentUser.website = it }
        profileRequest.bio?.let { currentUser.bio = it }
        userRepository.save(currentUser)
    }

    fun getUser(currentUser: User, nickname: String): User {
        val user = userRepository.findByNickname(nickname) ?: throw UserDoesNotExistException("invalid nickname")
        if (!user.public) {
            user.follower.find { it.email == user.email } ?: throw UserException("not public")
        }
        return user
    }
}
