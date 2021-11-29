package waffle.team3.wafflestagram

import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WafflestagramApplication

fun main(args: Array<String>) {
    runApplication<WafflestagramApplication>(*args)
}
