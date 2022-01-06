package waffle.team3.wafflestagram.domain.User.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserException
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun signup(signupRequest: UserDto.SignupRequest): User {
        if (userRepository.findByEmail(signupRequest.email) != null)
            throw UserException("this email already exists")
        signupRequest.nickname?.let {
            val nicknameUser = userRepository.findByNickname(it)
            if (nicknameUser != null)
                throw UserException("this nickname already exists")
        }
        return userRepository.save(
            User(
                email = signupRequest.email,
                password = passwordEncoder.encode(signupRequest.password),
                public = signupRequest.public,
                name = signupRequest.name,
                nickname = signupRequest.nickname,
                birthday = signupRequest.birthday,
                phoneNumber = signupRequest.phoneNumber,
            )
        )
    }

    @Transactional
    fun setProfile(user: User, profileRequest: UserDto.ProfileRequest) {
        val currentUser = userRepository.findByIdOrNull(user.id)!!
        profileRequest.public?.let { currentUser.public = it }
        profileRequest.name?.let { currentUser.name = it }
        profileRequest.nickname?.let {
            val nicknameUser = userRepository.findByNickname(profileRequest.nickname)
            if (nicknameUser != null && nicknameUser.id != currentUser.id)
                throw UserException("this nickname already exists")
            else currentUser.nickname = it
        }
        profileRequest.website?.let { currentUser.website = it }
        profileRequest.bio?.let { currentUser.bio = it }
        profileRequest.birthday?.let { currentUser.birthday = it }
        profileRequest.phoneNumber?.let { currentUser.phoneNumber = it }
        userRepository.save(currentUser)
    }

    @Transactional
    fun getUser(currentUser: User, nickname: String): User {
        val user = userRepository.findByNickname(nickname) ?: throw UserDoesNotExistException("invalid nickname")
        if (!user.public) {
            user.follower.find { it.user.email == user.email } ?: throw UserException("not public")
        }
        return user
    }

    fun getUserById(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun saveUser(user: User) {
        userRepository.save(user)
    }
}
