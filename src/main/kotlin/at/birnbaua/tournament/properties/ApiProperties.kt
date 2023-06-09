package at.birnbaua.tournament.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "api")
@Suppress("unused")
class ApiProperties {
    var tournament: String = "tournaments"
    var gameround: String = "gamerounds"
    var field: String = "fields"
    var team: String = "teams"
    var match: String = "matches"
}