package waffle.team3.wafflestagram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
@PropertySources(
    *[
        PropertySource("classpath:application-s3.properties"),
        PropertySource("classpath:application-oauth.properties")
    ]
)
class WafflestagramApplication

fun main(args: Array<String>) {
    runApplication<WafflestagramApplication>(*args)
}
