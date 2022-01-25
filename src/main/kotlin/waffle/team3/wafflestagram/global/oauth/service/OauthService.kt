package waffle.team3.wafflestagram.global.oauth.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import waffle.team3.wafflestagram.domain.User.service.UserService
import waffle.team3.wafflestagram.global.oauth.SocialLoginType
import waffle.team3.wafflestagram.global.oauth.exception.AccessTokenException
import waffle.team3.wafflestagram.global.oauth.exception.InvalidArgException
import waffle.team3.wafflestagram.global.oauth.exception.InvalidTokenException
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@Service
class OauthService(
    private val facebookOauth: FacebookOauth,
    private val response: HttpServletResponse,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    @Value("\${facebook.verify.token.url}")
    private lateinit var facebook_verify_token_url: String

    @Value("\${facebook.client.id}")
    private lateinit var facebook_app_id: String

    @Value("\${facebook.client.secret}")
    private lateinit var facebook_app_secret: String

    @Value("\${facebook.info.id.url}")
    private lateinit var facebook_info_id_url: String

    @Value("\${facebook.token.url}")
    private lateinit var facebook_token_url: String

    @Value("\${facebook.callback.url}")
    private lateinit var facebook_callback_url: String

    fun request(socialLoginType: SocialLoginType) {
        var redirectURL: String
        when (socialLoginType) {
            SocialLoginType.facebook -> redirectURL = facebookOauth.getOauthRedirectURL()
            else -> throw InvalidArgException("Invalid Social Login format")
        }
        try {
            response.sendRedirect(redirectURL)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun findUser(socialLoginType: SocialLoginType, token: String): User {
        when (socialLoginType) {
            SocialLoginType.facebook -> return facebookOauth.findUser(token)
            else -> throw InvalidArgException("Invalid Social Login format")
        }
    }

    fun findUserNameAndEmail(idToken: String): java.util.HashMap<*, *>? {
        val restTemplate = RestTemplateBuilder().build()
        val builder = UriComponentsBuilder.fromHttpUrl(facebook_info_id_url)
            .queryParam("fields", "email,name")
            .queryParam("access_token", idToken)

        val response = restTemplate.exchange<String>(
            builder.toUriString(),
            HttpMethod.GET
        )
        if (response.statusCode != HttpStatus.OK) throw InvalidTokenException("Wrong validation")
        return objectMapper.readValue(response.body, HashMap::class.java)
    }

    fun requestAccessToken(): String {
        val restTemplate = RestTemplateBuilder().build()
        val builder = UriComponentsBuilder.fromHttpUrl(facebook_token_url)
            .queryParam("client_id", facebook_app_id)
            .queryParam("client_secret", facebook_app_secret)
            .queryParam("grant_type", "client_credentials")
            .queryParam("redirect_uri", facebook_callback_url)

        val response = restTemplate.exchange<String>(
            builder.toUriString(),
            HttpMethod.GET
        )

        if (response.statusCode == HttpStatus.OK) {
            val hashmap = objectMapper.readValue(response.body, HashMap::class.java)
            return hashmap["access_token"].toString()
        } else throw AccessTokenException("Wrong request")
    }

    fun verifyAccessToken(token: String): User {
        val accessToken = requestAccessToken()
        val restTemplate = RestTemplateBuilder().build()
        val builder = UriComponentsBuilder.fromHttpUrl(facebook_verify_token_url)
            .queryParam("input_token", token)
            .queryParam("access_token", accessToken)

        val response = restTemplate.exchange<String>(
            builder.toUriString(),
            HttpMethod.GET,
        )

        if (response.statusCode != HttpStatus.OK) throw InvalidTokenException("Wrong validation")

        val userNameAndEmail = findUserNameAndEmail(token)
            ?: throw UserDoesNotExistException("User with this email does not exist.")

        val email = userNameAndEmail["email"]

        return userRepository.findByEmail(email as String)
            ?: userService.signup(
                UserDto.SignupRequest(
                    email = userNameAndEmail["email"] as String,
                    name = userNameAndEmail["name"] as String,
                    password = ""
                )
            )
    }
}
