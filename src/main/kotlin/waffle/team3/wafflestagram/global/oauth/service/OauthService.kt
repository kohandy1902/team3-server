package waffle.team3.wafflestagram.global.oauth.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.global.oauth.OauthToken
import waffle.team3.wafflestagram.global.oauth.SocialLoginType
import waffle.team3.wafflestagram.global.oauth.exception.InvalidArgException
import waffle.team3.wafflestagram.global.oauth.exception.InvalidTokenException
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@Service
class OauthService(
    private val googleOauth: GoogleOauth,
    private val facebookOauth: FacebookOauth,
    private val response: HttpServletResponse,
    private val objectMapper: ObjectMapper
) {
    @Value("\${facebook.verify.token.url}")
    private lateinit var facebook_verify_token_url: String

    @Value("\${facebook.client.id}")
    private lateinit var facebook_app_id: String

    @Value("\${facebook.info.id.url}")
    private lateinit var facebook_info_id_url: String

    fun request(socialLoginType: SocialLoginType) {
        var redirectURL: String
        when (socialLoginType) {
            SocialLoginType.google -> redirectURL = googleOauth.getOauthRedirectURL()
            SocialLoginType.facebook -> redirectURL = facebookOauth.getOauthRedirectURL()
            else -> throw InvalidArgException("Invalid Social Login format")
        }
        try {
            response.sendRedirect(redirectURL)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun requestAccessToken(socialLoginType: SocialLoginType, code: String): OauthToken {
        when (socialLoginType) {
            SocialLoginType.google -> return googleOauth.requestAccessToken(code)
            SocialLoginType.facebook -> return facebookOauth.requestAccessToken(code)
            else -> throw InvalidArgException("Invalid Social Login format")
        }
    }

    fun findUser(socialLoginType: SocialLoginType, token: String): User {
        when (socialLoginType) {
            SocialLoginType.google -> return googleOauth.findUser(token)
            SocialLoginType.facebook -> return facebookOauth.findUser(token)
            else -> throw InvalidArgException("Invalid Social Login format")
        }
    }

    fun findUserId(token: String): String {
        val restTemplate = RestTemplateBuilder().build()
        val paraMap = mutableMapOf<String, String?>()
        paraMap["fields"] = "id"
        paraMap["access_token"] = token

        val idEntity = restTemplate.getForEntity<String>(facebook_info_id_url, paraMap)
        if (idEntity.statusCode == HttpStatus.OK) throw InvalidTokenException("Wrong validation")
        val hashmap = objectMapper.readValue(idEntity.body, HashMap::class.java)

        return hashmap["user_id"].toString()
    }

    fun verifyAccessToken(token: String): User {
        val restTemplate = RestTemplateBuilder().build()
        val paraMap = mutableMapOf<String, String>()
        val accessToken = facebookOauth.requestAppAccessToken()

        val link = facebook_verify_token_url + "input_token=" + token + "&access_token=" + accessToken

        val validateEntity = restTemplate.getForEntity(link, String::class.java)
        val userId = findUserId(accessToken)
        if (validateEntity.statusCode != HttpStatus.OK) throw InvalidTokenException("Wrong validation")
        val hashmap = objectMapper.readValue(validateEntity.body, HashMap::class.java)

        if (!(hashmap["app_id"] == facebook_app_id && hashmap["user_id"] == userId)) throw InvalidTokenException("Wrong validation")
        return facebookOauth.findUser(accessToken)
    }
}
