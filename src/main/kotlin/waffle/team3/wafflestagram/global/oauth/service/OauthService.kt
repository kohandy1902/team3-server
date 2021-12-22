package waffle.team3.wafflestagram.global.oauth.service

import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.global.oauth.OauthToken
import waffle.team3.wafflestagram.global.oauth.SocialLoginType
import waffle.team3.wafflestagram.global.oauth.exception.InvalidArgException
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@Service
class OauthService(
    private val googleOauth: GoogleOauth,
    private val facebookOauth: FacebookOauth,
    private val response: HttpServletResponse,
) {
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

    fun findUser(socialLoginType: SocialLoginType, token: OauthToken): User {
        when (socialLoginType) {
            SocialLoginType.google -> return googleOauth.findUser(token)
            SocialLoginType.facebook -> return facebookOauth.findUser(token)
            else -> throw InvalidArgException("Invalid Social Login format")
        }
    }
}
