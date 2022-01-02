package waffle.team3.wafflestagram.domain.User.api

import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.User.dto.UserDto
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
    fun signup(@Valid @RequestBody signupRequest: UserDto.SignupRequest): ResponseEntity<*> {
        val user = userService.signup(signupRequest)
        return ResponseEntity.ok().header("Authentication", jwtTokenProvider.generateToken(user.email)).body(null)
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
    fun getProfile(@CurrentUser currentUser: User, @RequestParam("nickname") nickname: String): ResponseEntity<UserDto.Response> {
        val user = userService.getUser(currentUser, nickname)
        return ResponseEntity.ok().body(UserDto.Response(user))
    }

    @PostMapping("/follow/{id}/")
    @Transactional
    fun followRequest(
        @CurrentUser user: User,
        @PathVariable("id") id: Long // id: User-id
    ): ResponseEntity<UserDto.Response> {
        val followUser = userService.getUserById(id)
        if (followUser == null || user.id == id) return ResponseEntity.badRequest().build()
        if (followUser.public) {
            followerUserService.addFollower(followUser, user)
            followingUserService.addFollowing(user, followUser)
            userService.saveUser(user)
            userService.saveUser(followUser)
        } else {
            waitingFollowerUserService.addWaitingFollower(followUser, user)
            userService.saveUser(followUser)
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("/approve/{id}/")
    fun approveRequest(
        @CurrentUser user: User,
        @PathVariable("id") id: Long // id: User-id
    ): ResponseEntity<UserDto.Response> {
        for (waitingFollower in user.waitingFollower) {
            if (waitingFollower.user.id == id) {
                followingUserService.addFollowing(waitingFollower.user, user)
                followerUserService.addFollower(user, waitingFollower.user)
                user.waitingFollower.remove(waitingFollower)
                userService.saveUser(user)
                return ResponseEntity.ok().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @PostMapping("/refuse/{id}/")
    fun refuseRequest(
        @CurrentUser user: User,
        @PathVariable("id") id: Long // id: User-id
    ): ResponseEntity<UserDto.Response> {
        for (waitingFollower in user.waitingFollower) {
            if (waitingFollower.user.id == id) {
                user.waitingFollower.remove(waitingFollower)
                userService.saveUser(user)
                return ResponseEntity.ok().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }
}
