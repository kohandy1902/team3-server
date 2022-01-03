package waffle.team3.wafflestagram.global.oauth.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import waffle.team3.wafflestagram.global.oauth.OauthToken
import waffle.team3.wafflestagram.global.oauth.exception.AccessTokenException

@Component
class GoogleOauth(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
) : SocialOauth {
    @Value("\${google.url}")
    private val google_base_auth_url: String? = null

    @Value("\${google.client.id}")
    private val google_client_id: String? = null

    @Value("\${google.client.secret}")
    private val google_client_secret: String? = null

    @Value("\${google.callback.url}")
    private val google_callback_url: String? = null

    @Value("\${google.token.url}")
    private val google_base_token_auth_url: String? = null

    @Value("\${google.info.url}")
    private val google_userinfo_url: String? = null

    @Override
    override fun getOauthRedirectURL(): String {
        val paramMap = mutableMapOf<String, String?>()
        paramMap.put("scope", "email")
        paramMap.put("response_type", "code")
        paramMap.put("client_id", google_client_id)
        paramMap.put("redirect_uri", google_callback_url)
        val parameterString = paramMap.map { it.key + '=' + it.value }.joinToString("&")
        return google_base_auth_url + "?" + parameterString
    }

    @Override
    override fun requestAccessToken(code: String): OauthToken {
        val restTemplate = RestTemplateBuilder().build()
        val paramMap = mutableMapOf<String, String?>()
        paramMap.put("code", code)
        paramMap.put("client_id", google_client_id)
        paramMap.put("client_secret", google_client_secret)
        paramMap.put("redirect_uri", google_callback_url)
        paramMap.put("grant_type", "authorization_code")

        val responseEntity = restTemplate.postForEntity(google_base_token_auth_url!!, paramMap, String::class.java)
        if (responseEntity.statusCode == HttpStatus.OK) {
            val hashmap = objectMapper.readValue(responseEntity.body, HashMap::class.java)
            return OauthToken(
                access_token = hashmap["access_token"].toString(),
                expires_in = hashmap["expires_in"].toString().toLong(),
                scope = hashmap["scope"].toString(),
                token_type = hashmap["token_type"].toString(),
                id_token = hashmap["id_token"].toString()
            )
        } else throw AccessTokenException("Access token Failed")
    }

    @Override
    override fun findUser(token: OauthToken): User {
        val restTemplate = RestTemplateBuilder().build()
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer ${token.access_token}")
        val request = HttpEntity<Map<String, String>>(headers)
        val responseEntity = restTemplate.exchange(google_userinfo_url!!, HttpMethod.GET, request, String::class.java)
        if (responseEntity.statusCode == HttpStatus.OK) {
            val hashmap = objectMapper.readValue(responseEntity.body, HashMap::class.java)
            return userRepository.findByEmail(hashmap["email"].toString()) ?: userRepository.save(User(email = hashmap["email"].toString()))
        } else throw AccessTokenException("Get user profile failed")
    }
}
