package at.birnbaua.tournament.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "api")
@Suppress("unused")
class ApiProperties {
    var tournament: String = "/tournament"
    var gameround: String = "/gameround"
    var field: String = "/field"
    var team: String = "/team"
    var match: String = "/match"
}