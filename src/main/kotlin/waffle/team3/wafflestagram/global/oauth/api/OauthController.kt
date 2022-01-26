package waffle.team3.wafflestagram.global.oauth.api

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import waffle.team3.wafflestagram.domain.User.service.UserService
import waffle.team3.wafflestagram.global.auth.JwtTokenProvider
import waffle.team3.wafflestagram.global.oauth.exception.AccessTokenException
import waffle.team3.wafflestagram.global.oauth.service.OauthService
import java.util.Collections

@RestController
@RequestMapping("/api/v1/social_login/")
class OauthController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val oauthService: OauthService,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @Value("\${google.client.id}")
    private val google_client_id: String? = null

    @PostMapping("/google/")
    fun googleTokenVerifier(
        @RequestHeader(value = "idToken") idToken: String
    ): ResponseEntity<UserDto.Response> {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(google_client_id))
            .build()

        val result: GoogleIdToken?
        try {
            result = verifier.verify(idToken)
        } catch (e: Exception) {
            throw AccessTokenException("token is invalid")
        }

        if (result != null) {
            val payload = result.payload
            if (userService.isAlreadyExists(payload.email))
                return ResponseEntity.ok()
                    .header("Authentication", jwtTokenProvider.generateToken(payload.email))
                    .body(UserDto.Response(userService.getUserByEmail(payload.email)))

            return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authentication", jwtTokenProvider.generateToken(payload.email))
                .body(UserDto.Response(userService.getUserByEmail(payload.email)))
        } else {
            throw AccessTokenException("token is invalid")
        }
    }

    @PostMapping("/facebook/verify/")
    fun verifyFacebook(
        @RequestHeader("idToken") idToken: String
    ): ResponseEntity<UserDto.Response> {
        try {
            val userNameAndEmail = oauthService.verifyAccessToken(idToken)
            val id = userNameAndEmail["id"] as String
            val name = userNameAndEmail["name"] as String

            // 이메일이 없는 경우
            if (userNameAndEmail["email"] == null) {
                val user = userService.signup(UserDto.SignupRequest(email = id, name = name, password = ""))
                return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Authentication", jwtTokenProvider.generateToken(user.email))
                    .body(UserDto.Response(user))
            }

            val email = userNameAndEmail["email"] as String
            var user = userRepository.findByEmail(email)
            return if (user != null) {
                ResponseEntity.status(HttpStatus.OK)
                    .header("Authentication", jwtTokenProvider.generateToken(user.email))
                    .body(UserDto.Response(user))
            } else {
                user = userService.signup(UserDto.SignupRequest(email = email, name = name, password = ""))
                ResponseEntity.status(HttpStatus.CREATED)
                    .header("Authentication", jwtTokenProvider.generateToken(user.email))
                    .body(UserDto.Response(user))
            }
        } catch (e: AccessTokenException) {
            throw AccessTokenException("token is invalid")
        }
    }
}
