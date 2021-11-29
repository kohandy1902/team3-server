package waffle.team3.wafflestagram.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
<<<<<<< HEAD
class SecurityConfig(

): WebSecurityConfigurerAdapter() {
=======
class SecurityConfig() : WebSecurityConfigurerAdapter() {
>>>>>>> f4a7ec03736c65f37d7653438586830f25ff7a0e

    override fun configure(auth: AuthenticationManagerBuilder?) {
        super.configure(auth)
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/v1/ping").permitAll()
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> f4a7ec03736c65f37d7653438586830f25ff7a0e
