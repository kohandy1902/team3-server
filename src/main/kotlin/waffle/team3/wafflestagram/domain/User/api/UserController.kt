package waffle.team3.wafflestagram.domain.User.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.service.UserService
import waffle.team3.wafflestagram.global.auth.CurrentUser
import waffle.team3.wafflestagram.global.auth.JwtTokenProvider
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/signup/")
    fun signup(@Valid @RequestBody signupRequest: UserDto.SignupRequest): ResponseEntity<*> {
        val user = userService.signup(signupRequest)
        return ResponseEntity.ok().header("Authentication", jwtTokenProvider.generateToken(user.email)).body(null)
    }

    @GetMapping("/me/")
    fun getme(@CurrentUser user: User): ResponseEntity<UserDto.Response> {
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
}
