package waffle.team3.wafflestagram.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import waffle.team3.wafflestagram.global.auth.JwtAuthenticationEntryPoint
import waffle.team3.wafflestagram.global.auth.JwtAuthenticationFilter
import waffle.team3.wafflestagram.global.auth.JwtTokenProvider
import waffle.team3.wafflestagram.global.auth.SigninAuthenticationFilter
import waffle.team3.wafflestagram.global.auth.model.UserPrincipalDetailService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userPrincipalDetailService: UserPrincipalDetailService
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        super.configure(auth)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
//        return Argon2PasswordEncoder()
        return BCryptPasswordEncoder()
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(userPrincipalDetailService)
        return provider
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .addFilter(SigninAuthenticationFilter(authenticationManager(), jwtTokenProvider))
            .addFilter(JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider))
            .authorizeRequests() { a ->
                a.antMatchers(
                    "/api/v1/ping/", "/api/v1/users/signin/", "/api/v1/users/signup/",
                    "/api/v1/social_login/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html", "/v3/api-docs", "/webjars/**", "/v2/api-docs",
                    "/swagger-ui/index.html"
                ).permitAll().anyRequest().authenticated()
            }
            .exceptionHandling { e -> e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
            .oauth2Login { o ->
                o.failureHandler { request: HttpServletRequest, response: HttpServletResponse?, exception: AuthenticationException ->
                    request.session.setAttribute("error.message", exception.message)
                    val handler: AuthenticationEntryPointFailureHandler? = null
                    assert(false)
                    handler!!.onAuthenticationFailure(request, response, exception)
                }
            }
            .logout()
            .logoutUrl("/api/v1/users/signout/")
            .logoutSuccessUrl("/swagger-ui.html")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
    }
}
