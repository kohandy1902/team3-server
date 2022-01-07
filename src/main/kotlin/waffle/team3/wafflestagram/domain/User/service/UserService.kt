package waffle.team3.wafflestagram.domain.User.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserException
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import waffle.team3.wafflestagram.global.s3.service.S3Service

@Service
class UserService(
    private val followingUserService: FollowingUserService,
    private val followerUserService: FollowerUserService,
    private val userRepository: UserRepository,
    private val s3Service: S3Service,
    private val passwordEncoder: PasswordEncoder,
) {
    @Value("\${cloud.aws.s3.photoURL_default}")
    lateinit var default_s3URL: String

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
                profilePhotoURL = default_s3URL,
            )
        )
    }

    @Transactional
    fun setProfile(user: User, profileRequest: UserDto.ProfileRequest) {
        val currentUser = userRepository.findByIdOrNull(user.id)!!

        if (!currentUser.public && profileRequest.public == true)
            flushWaitingFollower(currentUser)

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

    @Value("\${cloud.aws.s3.photoURL}")
    lateinit var s3URL: String

    @Transactional
    fun setProfilePhoto(user: User, profilePhotoRequest: UserDto.ProfilePhotoRequest): String? {
        val currentUser = userRepository.findByIdOrNull(user.id) ?: throw UserDoesNotExistException("invalid user id")
        // currentUser.profilePhotoKey?.let { s3Service.deleteObj(it) }
        currentUser.profilePhotoKey = profilePhotoRequest.profilePhotoKey
        currentUser.profilePhotoURL = s3URL + currentUser.profilePhotoKey
        userRepository.save(currentUser)
        return currentUser.profilePhotoURL
    }

    fun getProfilePhoto(userId: Long): String {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserDoesNotExistException("invalid user id")
        return user.profilePhotoURL
    }

    @Transactional
    fun flushWaitingFollower(user: User) {
        for (waiting in user.waitingFollower) {
            followingUserService.addFollowing(waiting.user, user)
            followerUserService.addFollower(user, waiting.user)
            saveUser(waiting.user)
        }
        user.waitingFollower.clear()
        saveUser(user)
    }

    @Transactional
    fun getUserByNickname(currentUser: User, nickname: String): User {
        val user = userRepository.findByNickname(nickname) ?: throw UserDoesNotExistException("invalid nickname")
        if (!user.public) {
            user.follower.find { it.user.id == currentUser.id } ?: throw UserException("not public")
        }
        return user
    }

    @Transactional
    fun getUserById(currentUser: User, id: Long): User {
        val user = userRepository.findByIdOrNull(id) ?: throw UserDoesNotExistException("invalid id")
        if (!user.public) {
            user.follower.find { it.user.id == currentUser.id } ?: throw UserException("not public")
        }
        return user
    }

    fun getUserById(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    fun saveUser(user: User) {
        userRepository.save(user)
    }

    fun searchUsersByNickname(nickname_prefix: String, pageable: Pageable): Page<User> {
        return userRepository.findByNicknameStartsWith(nickname_prefix, pageable) ?: Page.empty()
    }
}
