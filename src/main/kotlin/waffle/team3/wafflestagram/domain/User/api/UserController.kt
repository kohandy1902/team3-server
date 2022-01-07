package waffle.team3.wafflestagram.domain.User.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.User.dto.FollowerUserDto
import waffle.team3.wafflestagram.domain.User.dto.FollowingUserDto
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.dto.WaitingFollowerUserDto
import waffle.team3.wafflestagram.domain.User.exception.FollowingUserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserException
import waffle.team3.wafflestagram.domain.User.exception.UserSignupException
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.service.FollowerUserService
import waffle.team3.wafflestagram.domain.User.service.FollowingUserService
import waffle.team3.wafflestagram.domain.User.service.UserService
import waffle.team3.wafflestagram.domain.User.service.WaitingFollowerUserService
import waffle.team3.wafflestagram.global.auth.CurrentUser
import waffle.team3.wafflestagram.global.auth.JwtTokenProvider
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val followerUserService: FollowerUserService,
    private val followingUserService: FollowingUserService,
    private val waitingFollowerUserService: WaitingFollowerUserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/signup/")
    fun signup(@Valid @RequestBody signupRequest: UserDto.SignupRequest): ResponseEntity<UserDto.Response> {
        return try {
            val user = userService.signup(signupRequest)
            ResponseEntity.ok().header("Authentication", jwtTokenProvider.generateToken(user.email)).body(UserDto.Response(user))
        } catch (e: UserException) {
            throw UserSignupException("email or nickname is duplicated")
        }
    }

    @GetMapping("/search/{nickname_prefix}/")
    fun search(
        @PathVariable("nickname_prefix") nickname_prefix: String,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
    ): ResponseEntity<Page<UserDto.Response>> {
        val users = userService.searchUsersByNickname(nickname_prefix, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(users.map { UserDto.Response(it) })
    }

    @PostMapping("/profilePhoto/")
    fun setProfilePhoto(
        @CurrentUser user: User,
        @RequestBody profilePhotoRequest: UserDto.ProfilePhotoRequest
    ): ResponseEntity<String> {
        val profilePhotoURL = userService.setProfilePhoto(user, profilePhotoRequest)
        return ResponseEntity.ok().body(profilePhotoURL)
    }

    @GetMapping("/profilePhoto/{user_id}/")
    fun getProfilePhoto(
        @PathVariable("user_id") userId: Long
    ): ResponseEntity<String> {
        val profilePhotoURL = userService.getProfilePhoto(userId)
        return ResponseEntity.ok().body(profilePhotoURL)
    }

    @GetMapping("/me/")
    fun getMe(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @PostMapping("/profile/")
    fun setProfile(@CurrentUser user: User, @RequestBody profileRequest: UserDto.ProfileRequest) {
        userService.setProfile(user, profileRequest)
    }

    @GetMapping("/profile/")
    fun getProfileByNickname(
        @CurrentUser currentUser: User,
        @RequestParam("nickname") nickname: String
    ): ResponseEntity<UserDto.Response> {
        val user = userService.getUserByNickname(currentUser, nickname)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @GetMapping("/profile/{user_id}/")
    fun getProfileById(
        @CurrentUser currentUser: User,
        @PathVariable("user_id") userId: Long,
    ): ResponseEntity<UserDto.Response> {
        val user = userService.getUserById(currentUser, userId)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @PostMapping("/follow/{user_id}/")
    fun followRequest(
        @CurrentUser user: User,
        @PathVariable("user_id") userId: Long,
    ): ResponseEntity<UserDto.Response> {
        val followUser = userService.getUserById(userId)
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        if (followUser == null || currUser.id == userId || currUser.following.any { it.user.id == userId })
            throw UserDoesNotExistException("user not exist")
        if (followUser.public) {
            followerUserService.addFollower(followUser, currUser)
            followingUserService.addFollowing(currUser, followUser)
            userService.saveUser(currUser)
            userService.saveUser(followUser)
        } else {
            waitingFollowerUserService.addWaitingFollower(followUser, currUser)
            userService.saveUser(followUser)
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/unfollow/{user_id}/")
    fun unfollowRequest(
        @CurrentUser user: User,
        @PathVariable("user_id") userId: Long,
    ): ResponseEntity<UserDto.Response> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        val followUser = userService.getUserById(userId) ?: throw UserDoesNotExistException("user not exist")
        val erase1 = currUser.following.removeIf { it.user.id == userId }
        val erase2 = followUser.follower.removeIf { it.user.id == currUser.id }
        if (erase1 && erase2) {
            userService.saveUser(currUser)
            userService.saveUser(followUser)
            return ResponseEntity.ok().build()
        }
        throw UserDoesNotExistException("Not following User")
    }

    @PostMapping("/approve/{user_id}/")
    fun approveRequest(
        @CurrentUser user: User,
        @PathVariable("user_id") userId: Long,
    ): ResponseEntity<UserDto.Response> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        for (waitingFollower in currUser.waitingFollower) {
            if (waitingFollower.user.id == userId) {
                followingUserService.addFollowing(waitingFollower.user, currUser)
                followerUserService.addFollower(currUser, waitingFollower.user)
                currUser.waitingFollower.remove(waitingFollower)
                userService.saveUser(currUser)
                userService.saveUser(waitingFollower.user)
                return ResponseEntity.ok().build()
            }
        }
        throw UserDoesNotExistException("Didn't Request Follow")
    }

    @PostMapping("/refuse/{user_id}/")
    fun refuseRequest(
        @CurrentUser user: User,
        @PathVariable("user_id") userId: Long,
    ): ResponseEntity<UserDto.Response> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        for (waitingFollower in currUser.waitingFollower) {
            if (waitingFollower.user.id == userId) {
                currUser.waitingFollower.remove(waitingFollower)
                userService.saveUser(currUser)
                return ResponseEntity.ok().build()
            }
        }
        throw UserDoesNotExistException("Didn't Request Follow")
    }

    @GetMapping("/isFollowing/{user_id}/")
    fun getIsFollowing(
        @CurrentUser user: User,
        @PathVariable("user_id") userId: Long,
    ): Boolean {
        val currUser = userService.getUserById(user.id) ?: return false
        return currUser.following.any { it.user.id == userId }
    }

    @GetMapping("/following/")
    fun getFollowingList(
        @CurrentUser user: User,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
    ): ResponseEntity<Page<FollowingUserDto.Response>> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        val result = convertSetToPage(currUser.following, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(
            result.map {
                FollowingUserDto.Response(it)
            }
        )
    }

    @GetMapping("/following/{user_id}/")
    fun getFollowingListByUser(
        @CurrentUser user: User,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
        @PathVariable("user_id") userId: Long,
    ): ResponseEntity<Page<FollowingUserDto.Response>> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        val otherUser = userService.getUserById(userId) ?: throw UserDoesNotExistException("user not exist")

        if(!otherUser.public && !otherUser.follower.any { it.user.id == currUser.id }) {
            throw FollowingUserDoesNotExistException("You are not follower of this user.")
        }

        val result = convertSetToPage(otherUser.following, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(
            result.map {
                FollowingUserDto.Response(it)
            }
        )
    }

    @GetMapping("/follower/")
    fun getFollowerList(
        @CurrentUser user: User,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
    ): ResponseEntity<Page<FollowerUserDto.Response>> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        val result = convertSetToPage(currUser.follower, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(
            result.map {
                FollowerUserDto.Response(it)
            }
        )
    }

    @GetMapping("/waiting/")
    fun getWaitingFollowerList(
        @CurrentUser user: User,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
    ): ResponseEntity<Page<WaitingFollowerUserDto.Response>> {
        val currUser = userService.getUserById(user.id) ?: return ResponseEntity.badRequest().build()
        val result = convertSetToPage(currUser.waitingFollower, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(
            result.map {
                WaitingFollowerUserDto.Response(it)
            }
        )
    }

    fun <E> convertSetToPage(set: MutableSet<E>, pageable: Pageable): Page<E> {
        val list = set.toList()
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(list.size)
        if (start > list.size) return PageImpl(listOf(), pageable, list.size.toLong())
        return PageImpl(list.subList(start, end), pageable, list.size.toLong())
    }
}
