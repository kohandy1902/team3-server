package waffle.team3.wafflestagram.global.oauth

class OauthToken(
    val access_token: String,
    val expires_in: Long,
    val scope: String,
    val token_type: String,
    val id_token: String,
)
