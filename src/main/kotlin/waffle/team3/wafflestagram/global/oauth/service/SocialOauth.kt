package waffle.team3.wafflestagram.global.oauth.service

import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.global.oauth.OauthToken

interface SocialOauth {
    fun getOauthRedirectURL(): String
    fun requestAccessToken(code: String): OauthToken
    fun findUser(token: String): User
}
