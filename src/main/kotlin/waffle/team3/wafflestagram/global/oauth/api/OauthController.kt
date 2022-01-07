package waffle.team3.wafflestagram.global.oauth.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.global.auth.JwtTokenProvider
import waffle.team3.wafflestagram.global.oauth.SocialLoginType
import waffle.team3.wafflestagram.global.oauth.service.OauthService

@RestController
@RequestMapping("/api/v1/social_login/")
class OauthController(
    private val oauthService: OauthService,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @GetMapping("/{socialLoginType}/")
    fun socialLogin(@PathVariable("socialLoginType") socialLoginType: SocialLoginType) {
        oauthService.request(socialLoginType)
    }
    @GetMapping("/{socialLoginType}/callback/")
    fun callback(
        @PathVariable("socialLoginType") socialLoginType: SocialLoginType,
        @RequestParam("code") code: String
    ): ResponseEntity<UserDto.Response> {
        val accessToken = oauthService.requestAccessToken(socialLoginType, code)
        val user = oauthService.findUser(socialLoginType, accessToken)
        return ResponseEntity.ok().header("Authentication", jwtTokenProvider.generateToken(user.email)).body(UserDto.Response(user))
    }
}
