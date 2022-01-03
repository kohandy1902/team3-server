package waffle.team3.wafflestagram.global.auth

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "user")
annotation class CurrentUser(val require: Boolean = true)
