package waffle.team3.wafflestagram.global.auth

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import waffle.team3.wafflestagram.domain.User.model.SignupType
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import waffle.team3.wafflestagram.global.auth.model.CustomAuthenticationToken
import waffle.team3.wafflestagram.global.auth.model.UserFilter
import waffle.team3.wafflestagram.global.auth.model.UserPrincipal
import java.util.Date

@Component
class JwtTokenProvider(private val userRepository: UserRepository) {
    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    val tokenPrefix = "Bearer "
    val headerString = "Authentication"

    @Value("\${app.jwt.jwt-secret-key}")
    private val jwtSecretKey: String? = null

    @Value("\${app.jwt.jwt-expiration-in-ms}")
    private val jwtExpirationInMs: Long? = null

    // Generate jwt token with prefix
    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal
        return generateToken(UserFilter(userPrincipal.user.email, userPrincipal.user.signupType).toString())
    }

    fun generateToken(s: String): String {
        val claims: MutableMap<String, Any> = hashMapOf("filter" to s)
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs!!)
        return tokenPrefix + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }

    fun getAuthenticationTokenFromJwt(token: String): Authentication {
        var tokenWithoutPrefix = token
        tokenWithoutPrefix = removePrefix(tokenWithoutPrefix)
        val claims = Jwts.parser()
            .setSigningKey(jwtSecretKey)
            .parseClaimsJws(tokenWithoutPrefix)
            .body

        // Recover User class from JWT
        val userFilter = UserFilter.parseUserFilter(claims.get("filter", String::class.java))
            ?: throw UsernameNotFoundException("Parse Error")
        val currentUser = userRepository.findByEmailAndSignupType(userFilter.email, userFilter.signupType)
            ?: throw UsernameNotFoundException("${userFilter.email} and ${userFilter.signupType} is not valid information, check token is expired")
        val userPrincipal = UserPrincipal(currentUser)
        val authorises = userPrincipal.authorities
        // Make token with parsed data
        return CustomAuthenticationToken(userPrincipal, null, authorises)
    }

    fun validateToken(authToken: String?): Boolean {
        if (authToken.isNullOrEmpty()) {
            logger.error("Token is not provided")
            return false
        }
        if (!authToken.startsWith(tokenPrefix)) {
            logger.error("Token not match type Bearer")
            return false
        }
        val authTokenWithoutPrefix = removePrefix(authToken)
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authTokenWithoutPrefix)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    fun removePrefix(tokenWithPrefix: String): String {
        return tokenWithPrefix.replace(tokenPrefix, "").trim { it <= ' ' }
    }
}
